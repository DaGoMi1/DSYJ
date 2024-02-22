package homepage.DSYJ.service;

import homepage.DSYJ.domain.Vote;
import homepage.DSYJ.repository.SpringDataJpaVoteRepository;

import java.time.LocalDateTime;

public class VoteService {
    private final SpringDataJpaVoteRepository voteRepository;

    public VoteService(SpringDataJpaVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void createVote(String topic, LocalDateTime startDate, LocalDateTime endDate) {
        // 투표 생성
        Vote newVote = new Vote();
        newVote.setTopic(topic);
        newVote.setStartDate(startDate);
        newVote.setEndDate(endDate);

        // 투표 저장
        voteRepository.save(newVote);
    }

    public Vote getVoteById(Long id) {
        return voteRepository.findById(id).orElse(null);
    }
}
