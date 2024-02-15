package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Member;
import homepage.DSYJ.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final MemberService memberService;

    @Autowired
    public RegisterController(MemberService memberService) {
        this.memberService = memberService;
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

        memberService.sendCodeToEmail(form.getEmail());

        if (memberService.isPasswordMatching(member.getPassword(), password2)) {
            memberService.join(member);
            return "redirect:/";
        } else {
            return "error";
        }
    }

    @PostMapping("/emails/verification")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String authCode) {
        boolean verificationResult = memberService.verifiedCode(email, authCode);

        if (verificationResult) {
            // 이메일 인증 성공
            // 원하는 동작 수행 (예: 회원 가입 처리)
            return new ResponseEntity<>("이메일 인증 성공", HttpStatus.OK);
        } else {
            // 이메일 인증 실패
            // 원하는 동작 수행 (예: 에러 응답)
            return new ResponseEntity<>("이메일 인증 실패", HttpStatus.BAD_REQUEST);
        }
    }

}
