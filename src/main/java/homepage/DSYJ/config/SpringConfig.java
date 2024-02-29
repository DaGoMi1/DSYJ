package homepage.DSYJ.config;

import homepage.DSYJ.repository.*;
import homepage.DSYJ.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class SpringConfig {
    private final SpringDataJpaPostingRepository springDataJpaPostingRepository;
    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;
    private final SpringDataJpaScheduleRepository springDataJpaScheduleRepository;
    private final SpringDataJpaCommentRepository springDataJpaCommentRepository;
    private final SpringDataJpaVoteRepository springDataJpaVoteRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public SpringConfig(SpringDataJpaPostingRepository springDataJpaPostingRepository,
                        SpringDataJpaMemberRepository springDataJpaMemberRepository,
                        SpringDataJpaScheduleRepository springDataJpaScheduleRepository,
                        SpringDataJpaCommentRepository springDataJpaCommentRepository,
                        SpringDataJpaVoteRepository springDataJpaVoteRepository,
                        JavaMailSender javaMailSender) {
        this.springDataJpaPostingRepository = springDataJpaPostingRepository;
        this.springDataJpaMemberRepository = springDataJpaMemberRepository;
        this.springDataJpaScheduleRepository = springDataJpaScheduleRepository;
        this.springDataJpaCommentRepository = springDataJpaCommentRepository;
        this.springDataJpaVoteRepository = springDataJpaVoteRepository;
        this.javaMailSender = javaMailSender;
    }

    @Bean
    public CommentService commentService() {
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

    @Bean
    public VoteService voteService(){
        return new VoteService(springDataJpaVoteRepository);
    }

    @Bean
    public EmailService emailService(){
        return new EmailService(javaMailSender);
    }

}
