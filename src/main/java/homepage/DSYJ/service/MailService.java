package homepage.DSYJ.service;

import homepage.DSYJ.domain.Member;
import homepage.DSYJ.repository.SpringDataJpaMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Transactional
public class MailService {

    private final JavaMailSender emailSender;
    private final SpringDataJpaMemberRepository memberRepository;
    private final RedisService redisService;

    @Autowired
    public MailService(JavaMailSender emailSender, SpringDataJpaMemberRepository memberRepository, RedisService redisService) {
        this.emailSender = emailSender;
        this.memberRepository = memberRepository;
        this.redisService = redisService;
    }

    public void sendEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);

        String title = "Data Science 홈페이지 이메일 인증 번호";

        String authCode = this.createCode();
        saveAuthCode(toEmail, authCode);

        SimpleMailMessage emailForm = createEmailForm(toEmail, title, authCode);
        try {
            emailSender.send(emailForm);
        } catch (MailException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, authCode);
            // 예외를 그대로 던질 수도 있습니다.
            throw e;
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    public void saveAuthCode(String email, String authCode) {
        String key = "AuthCode " + email;
        redisService.saveData(key, authCode);
    }

    public String getAuthCode(String email) {
        String key = "AuthCode " + email;
        return redisService.getData(key);
    }

    public boolean checkExistsValue(String authCode) {
        // authCode가 null이 아니고 비어있지 않으면 Redis에 값이 존재한다고 판단
        return authCode != null && !authCode.isEmpty();
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new IllegalArgumentException("Invalid argument");
        }
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new IllegalArgumentException("Invalid argument");
        }
    }

}