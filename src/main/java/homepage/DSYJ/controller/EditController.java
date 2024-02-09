package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Member;
import homepage.DSYJ.dto.CustomUserDetails;
import homepage.DSYJ.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EditController {

    private final MemberService memberService;

    @Autowired
    public EditController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/edit")
    public String myHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Member loggedInMember = userDetails.getMember();
        // 여기서 name을 기반으로 Member 정보를 데이터베이스에서 가져와서 model에 추가하는 작업을 수행하면 됩니다.
        // ...

        model.addAttribute("name", loggedInMember.getName());
        model.addAttribute("email", loggedInMember.getEmail());
        // 나머지 필요한 정보를 가져와서 model에 추가

        return "myPage";
    }

    @PostMapping("/updateUserInformation")
    public String editUser(@ModelAttribute MemberForm form,
                           @RequestParam String password2,
                           @RequestParam String password3,
                           Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Member loggedInMember = userDetails.getMember();

        loggedInMember.setName(updateField(loggedInMember.getName(), form.getName()));
        loggedInMember.setEmail(updateField(loggedInMember.getEmail(), form.getEmail()));

        if (!form.getPassword().isEmpty() && !password2.isEmpty() && !password3.isEmpty()) {
            if (form.getPassword().equals(loggedInMember.getPassword())
                    && password2.equals(password3)) {
                loggedInMember.setPassword(password2);
            }
        }
        // DB에 반영
        memberService.updateMember(loggedInMember);
        // 리다이렉트
        return "redirect:/";
    }

    private String updateField(String current, String newValue) {
        return (!newValue.isEmpty()) ? newValue : current;
    }
}
