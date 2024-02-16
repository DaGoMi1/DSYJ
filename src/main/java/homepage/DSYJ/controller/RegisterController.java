package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Member;
import homepage.DSYJ.service.MailService;
import homepage.DSYJ.service.MemberService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {

    private final MemberService memberService;
    private final MailService mailService;

    @Autowired
    public RegisterController(MemberService memberService, MailService mailService) {
        this.memberService = memberService;
        this.mailService = mailService;
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register-process")
    public String passwordChecking(@ModelAttribute MemberForm form,
                                   @RequestParam String password2) {
        Member member = new Member();
        member.setName(form.getName());
        member.setEmail(form.getEmail());
        member.setUserId(form.getUserId());
        member.setPassword(form.getPassword());

        if (memberService.isPasswordMatching(member.getPassword(), password2)) {
            memberService.join(member);
            return "redirect:/";
        } else {
            return "error";
        }
    }

    // Request 객체 정의
    @Getter
    public static class VerificationCodeRequest {
        private String email;
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerificationCodeRequest request) {
        String email = request.getEmail();
        // 여기에서 이메일 전송 및 인증코드 생성 로직을 수행
        mailService.sendEmail(email);
        // 응답 메시지
        Map<String, String> response = new HashMap<>();
        response.put("message", "인증코드를 이메일로 전송했습니다.");

        return ResponseEntity.ok(response);
    }
}
