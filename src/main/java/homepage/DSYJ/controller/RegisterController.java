package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Member;
import homepage.DSYJ.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        if (memberService.isPasswordMatching(member.getPassword(), password2)) {
            memberService.join(member);
            return "redirect:/";
        } else {
            return "error";
        }
    }
}
