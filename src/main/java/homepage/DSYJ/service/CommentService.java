package homepage.DSYJ.service;

import homepage.DSYJ.domain.Comment;
import homepage.DSYJ.repository.SpringDataJpaCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentService {
    private final SpringDataJpaCommentRepository commentRepository;

    @Autowired
    public CommentService(SpringDataJpaCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment){
        commentRepository.save(comment);
    }
    public List<Comment> findByPostingId(Long id) {
        return commentRepository.findByPostingId(id);
    }
}
