package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Posting;
import homepage.DSYJ.service.PostingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    private final PostingService postingService;

    public HomeController(PostingService postingService) {
        this.postingService = postingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Posting> freeBoardList = postingService.findByBoardType("free"); // 자유게시판 목록을 가져온다
        model.addAttribute("freeBoardList", freeBoardList);
        return "home";
    }
}

