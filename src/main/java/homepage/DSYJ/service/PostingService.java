package homepage.DSYJ.service;

import homepage.DSYJ.domain.Posting;
import homepage.DSYJ.repository.SpringDataJpaPostingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Transactional
public class PostingService {
    private final SpringDataJpaPostingRepository postingRepository;

    @Autowired
    public PostingService(SpringDataJpaPostingRepository postingRepository) {
        this.postingRepository = postingRepository;
    }

    public void postSave(Posting posting) {
        postingRepository.save(posting);
    }

    public void postUpdate(Posting updatePosting) {
        Posting existingPosting = postingRepository.findById(updatePosting.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        existingPosting.setTitle(updatePosting.getTitle());
        existingPosting.setAuthor(updatePosting.getAuthor());
        existingPosting.setContent(updatePosting.getContent());
        existingPosting.setImagePath(updatePosting.getImagePath());
        existingPosting.setVideoPath(updatePosting.getVideoPath());
        existingPosting.setFilePath(updatePosting.getFilePath());
        existingPosting.setLink(updatePosting.getLink());
        existingPosting.setCreated_at(LocalDateTime.now());

        postingRepository.save(existingPosting);
    }

    public Optional<Posting> findById(Long id) {
        return postingRepository.findById(id);
    }

    public List<Posting> findAll() {
        return postingRepository.findAll();
    }

    public List<Posting> findByBoardType(String boardType) {
        return postingRepository.findByBoardType(boardType);
    }

    public void deletePost(Long id){
        postingRepository.deleteById(id);
    }

    @Value("${file.upload.directory}")
    private String uploadDirectory;  // application.properties에서 설정된 파일 업로드 디렉토리

    public String saveImageAndReturnPath(MultipartFile image) {
        try {
            String fileName = UUID.randomUUID() + ".png";

            // 이미지를 서버에 저장
            Path destinationDirectory = Paths.get(uploadDirectory, "image");

            Path destination = destinationDirectory.resolve(fileName);
            Files.copy(image.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "/image/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 저장 실패 처리 로직 추가
            return null;
        }
    }

    public String saveFileAndReturnPath(MultipartFile file) {
        try {
            String fileName = "file_" + System.currentTimeMillis() + ".pdf";

            // 파일을 서버에 저장
            Path destinationDirectory = Paths.get(uploadDirectory, "file");

            Path destination = destinationDirectory.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "/file/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 저장 실패 처리 로직 추가
            return null;
        }
    }

    public String saveVideoAndReturnPath(MultipartFile video) {
        try {
            String fileName = "video_" + UUID.randomUUID() + ".avi";

            // 비디오를 서버에 저장
            Path destinationDirectory = Paths.get(uploadDirectory, "video");

            Path destination = destinationDirectory.resolve(fileName);
            Files.copy(video.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "/video/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 저장 실패 처리 로직 추가
            return null;
        }
    }
}