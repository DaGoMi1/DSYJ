package homepage.DSYJ.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String text) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (MailException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
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
        authCodeRepository.save(key, authCode);
    }

    public String getAuthCode(String email) {
        String key = "AuthCode " + email;
        return authCodeRepository.findById(key).orElse(null);
    }

    public boolean checkExistsValue(String authCode) {
        // authCode가 null이 아니고 비어있지 않으면 Redis에 값이 존재한다고 판단
        return authCode != null && !authCode.isEmpty();
    }
}