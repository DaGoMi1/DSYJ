package homepage.DSYJ.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/DI")
public class DepartmentIntroductionController {
    @GetMapping("/student")
    public String student() {
        return "student";
    }

    @GetMapping("/major")
    public String major() {
        return "major";
    }

}
