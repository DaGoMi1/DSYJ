package homepage.DSYJ.controller;

import homepage.DSYJ.domain.Posting;
import homepage.DSYJ.dto.CustomUserDetails;
import homepage.DSYJ.service.PostingService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    private final PostingService postingService;

    public NoticeController(PostingService postingService) {
        this.postingService = postingService;
    }

    @GetMapping("/notice")
    public String notice(Model model) {
        List<Posting> notice = postingService.findByBoardType("notice");

        model.addAttribute("postings", notice);
        model.addAttribute("boardType", "notice");
        return "boardList";
    }

    @GetMapping("/vote")
    public String vote(Model model) {
        return "vote";
    }

    @GetMapping("/write")
    public String write(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (customUserDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // 현재 사용자가 ADMIN인 경우에만 글쓰기 페이지로 이동
            Posting posting = new Posting();
            posting.setBoardType("notice");
            // 빈 폼을 렌더링하기 위해 빈 Posting 객체를 전달
            model.addAttribute("posting", posting);
            model.addAttribute("editable", false);  // 수정 가능한 상태로 설정
            return "write";
        } else {
            // 그 외의 경우에는 접근 거부 페이지 또는 다른 처리
            return "access-denied";
        }
    }

    @PostMapping("/submit_post")
    public String submitNotice(@ModelAttribute("postForm") Posting posting,
                               @RequestParam("image") MultipartFile image,
                               @RequestParam("video") MultipartFile video,
                               @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        posting.setUserId(userDetails.getUsername());
        posting.setCreated_at(LocalDateTime.now());
        posting.setBoardType("notice");
        if (posting.getLink().isBlank()) {
            posting.setLink(null);
        }

        // 파일 업데이트 메서드 호출
        updateFilePaths(posting, image, video, file);

        postingService.postSave(posting);
        return "redirect:/notice/notice";
    }

    @PostMapping("/save_edit")
    public String saveEdit(@ModelAttribute PostingForm form,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam("video") MultipartFile video,
                           @RequestParam("file") MultipartFile file) {
        Posting updatedPosting = new Posting();

        BeanUtils.copyProperties(form, updatedPosting);
        updatedPosting.setCreated_at(LocalDateTime.now());
        if (updatedPosting.getLink().isBlank()) {
            updatedPosting.setLink(null);
        }

        // 파일 업데이트 메서드 호출
        updateFilePaths(updatedPosting, image, video, file);

        // 저장된 게시글 업데이트
        postingService.postUpdate(updatedPosting);

        return "redirect:/notice/notice";
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

    @GetMapping("/notice/{id}")
    public String viewNotice(@PathVariable Long id, Model model) {
        Optional<Posting> postingOptional = postingService.findById(id);

        // Optional이 값이 있는 경우에만 모델에 추가
        Posting posting = postingOptional.orElse(null);
        model.addAttribute("posting", posting);

        // 상세 보기 페이지로 이동
        return "boardDetail";
    }

    @GetMapping("/edit")
    public String editNotice(@RequestParam("postId") Long postId, Model model) {
        // postId를 사용하여 게시글 정보를 가져오는 코드
        Posting existingPosting = postingService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 게시글을 찾을 수 없습니다: " + postId));
        ; // 이 메서드는 게시글 ID로 게시글을 조회하는 메서드로 가정

        model.addAttribute("posting", existingPosting);
        model.addAttribute("editable", true);  // 수정 가능한 상태로 설정
        return "write";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("postId") Long postId) {
        postingService.deletePost(postId);
        return "redirect:/notice/notice";
    }

}
