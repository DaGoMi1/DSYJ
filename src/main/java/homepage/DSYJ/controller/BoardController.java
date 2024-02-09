package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Comment;
import homepage.DSYJ.domain.Posting;
import homepage.DSYJ.dto.CustomUserDetails;
import homepage.DSYJ.service.CommentService;
import homepage.DSYJ.service.PostingService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final PostingService postingService;
    private final CommentService commentService;

    public BoardController(PostingService postingService, CommentService commentService) {
        this.postingService = postingService;
        this.commentService = commentService;
    }

    @GetMapping("/{boardType}")
    public String boardList(@PathVariable String boardType, Model model) {
        List<Posting> postings = postingService.findByBoardType(boardType);
        model.addAttribute("postings", postings);
        model.addAttribute("boardType", boardType);
        return "boardList";  // "boardList"�� ����� �� ������ �̸����� �����մϴ�.
    }


    @GetMapping("/write")
    public String write(@RequestParam(name = "boardType") String boardType, Model model) {
        Posting posting = new Posting();
        posting.setBoardType(boardType);

        model.addAttribute("posting", posting);
        model.addAttribute("editable", false);  // ���� ������ ���·� ����
        return "write";
    }

    @PostMapping("/submit_post")
    public String submitPost(@ModelAttribute("postForm") Posting posting,
                             @RequestParam("boardType") String boardType,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("video") MultipartFile video,
                             @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        posting.setUserId(userDetails.getUsername());
        posting.setCreated_at(LocalDateTime.now());
        posting.setBoardType(boardType);
        if (posting.getLink().isBlank()) {
            posting.setLink(null);
        }

        // 파일 업데이트 메서드 호출
        updateFilePaths(posting, image, video, file);

        postingService.postSave(posting);
        return "redirect:/board/" + boardType;
    }

    @PostMapping("/save_edit")
    public String saveEdit(@ModelAttribute PostingForm form,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam("video") MultipartFile video,
                           @RequestParam("file") MultipartFile file) {
        Posting updatedPosting = new Posting();

        BeanUtils.copyProperties(form, updatedPosting);
        updatedPosting.setCreated_at(LocalDateTime.now());
        String boardType = form.getBoardType();
        if (updatedPosting.getLink().isBlank()) {
            updatedPosting.setLink(null);
        }

        // 파일 업데이트 메서드 호출
        updateFilePaths(updatedPosting, image, video, file);

        postingService.postUpdate(updatedPosting);

        return "redirect:/board/" + boardType;
    }

    private void updateFilePaths(Posting posting, MultipartFile image, MultipartFile video, MultipartFile file) {
        // 파일 업로드 및 저장
        String imagePath = StringUtils.isNotBlank(image.getOriginalFilename()) ? postingService.saveImageAndReturnPath(image) : null;
        String videoPath = StringUtils.isNotBlank(video.getOriginalFilename()) ? postingService.saveVideoAndReturnPath(video) : null;
        String filePath = StringUtils.isNotBlank(file.getOriginalFilename()) ? postingService.saveFileAndReturnPath(file) : null;

        // 엔터티의 해당 필드 업데이트
        posting.setImagePath(imagePath);
        posting.setVideoPath(videoPath);
        posting.setFilePath(filePath);
    }

    @GetMapping("/board/{id}")
    public String viewFree(@PathVariable Long id, Model model) {
        Optional<Posting> postingOptional = postingService.findById(id);

        Posting posting = postingOptional.orElse(null);
        model.addAttribute("posting", posting);

        List<Comment> comments = commentService.findByPostingId(id);
        model.addAttribute("comments", comments);

        return "boardDetail";
    }

    @GetMapping("/edit")
    public String editPost(@RequestParam("postId") Long postId, Model model) {
        Posting existingPosting = postingService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("ID�� �ش��ϴ� �Խñ��� ã�� �� �����ϴ�: " + postId));

        model.addAttribute("posting", existingPosting);
        model.addAttribute("editable", true);
        return "write";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("postId") Long postId) {
        Optional<Posting> optionalPosting = postingService.findById(postId);

        String boardType = optionalPosting.map(Posting::getBoardType).orElse("defaultBoardType");
        postingService.deletePost(postId);
        return "redirect:/board/" + boardType;
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam Long postId,
                             @RequestParam String userId,
                             @RequestParam String comment) {
        Posting posting = postingService.findById(postId).orElse(null);

        if (posting != null) {
            Comment newComment = new Comment();
            newComment.setPosting(posting);
            newComment.setUserId(userId);
            newComment.setComment(comment);
            newComment.setCreatedDate(LocalDateTime.now());

            commentService.save(newComment);
        }

        return "redirect:/board/board/" + postId;
    }


}
