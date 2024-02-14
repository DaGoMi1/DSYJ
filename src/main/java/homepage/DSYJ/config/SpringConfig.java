package homepage.DSYJ.config;

import homepage.DSYJ.repository.SpringDataJpaCommentRepository;
import homepage.DSYJ.repository.SpringDataJpaMemberRepository;
import homepage.DSYJ.repository.SpringDataJpaPostingRepository;
import homepage.DSYJ.repository.SpringDataJpaScheduleRepository;
import homepage.DSYJ.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final SpringDataJpaPostingRepository springDataJpaPostingRepository;
    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;
    private final SpringDataJpaScheduleRepository springDataJpaScheduleRepository;
    private final SpringDataJpaCommentRepository springDataJpaCommentRepository;

    @Autowired
    public SpringConfig(SpringDataJpaPostingRepository springDataJpaPostingRepository,
                        SpringDataJpaMemberRepository springDataJpaMemberRepository,
                        SpringDataJpaScheduleRepository springDataJpaScheduleRepository,
                        SpringDataJpaCommentRepository springDataJpaCommentRepository) {
        this.springDataJpaPostingRepository = springDataJpaPostingRepository;
        this.springDataJpaMemberRepository = springDataJpaMemberRepository;
        this.springDataJpaScheduleRepository = springDataJpaScheduleRepository;
        this.springDataJpaCommentRepository = springDataJpaCommentRepository;
    }

    @Bean
    public CommentService commentService(){
        return new CommentService(springDataJpaCommentRepository);
    }
    @Bean
    public MemberService memberService() {
        return new MemberService(springDataJpaMemberRepository);
    }

    @Bean
    public PostingService postingService() {
        return new PostingService(springDataJpaPostingRepository);
    }

    @Bean
    public ScheduleService scheduleService() {
        return new ScheduleService(springDataJpaScheduleRepository);
    }
}
