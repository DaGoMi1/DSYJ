package homepage.DSYJ.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Random;

public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public int send(String email) {
        try {
            String title = "한국해양대학교 Data Science 홈페이지 인증번호";
            int authCode = generateRandomCode(); // 랜덤 코드 생성
            String content = "인증번호는" + authCode + "입니다.";
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(message, "UTF-8");

            mailHelper.setTo(email);
            mailHelper.setSubject(title);
            mailHelper.setText(content);

            javaMailSender.send(message);
            return authCode;
        } catch (MessagingException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private int generateRandomCode() {
        Random random = new Random();
        int min = (int) Math.pow(10, 6 - 1);
        int max = (int) Math.pow(10, 6) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
