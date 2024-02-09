package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Member;
import homepage.DSYJ.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Member loggedInMember = userDetails.getMember();
            model.addAttribute("loggedInUserName", loggedInMember.getName());
            model.addAttribute("loggedInUserId", loggedInMember.getUserId());
        }
    }
}
