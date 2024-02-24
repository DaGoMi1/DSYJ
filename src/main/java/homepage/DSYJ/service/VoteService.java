package homepage.DSYJ.service;

import homepage.DSYJ.domain.Option;
import homepage.DSYJ.domain.Vote;
import homepage.DSYJ.repository.SpringDataJpaOptionRepository;
import homepage.DSYJ.repository.SpringDataJpaVoteRepository;

import java.time.LocalDateTime;
import java.util.List;

public class VoteService {
    private final SpringDataJpaVoteRepository voteRepository;
    private final SpringDataJpaOptionRepository optionRepository;

    public VoteService(SpringDataJpaVoteRepository voteRepository,
                       SpringDataJpaOptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }

    public void createVote(String topic, LocalDateTime startDate,
                           LocalDateTime endDate, List<String> options) {
        // Vote 엔티티 생성 및 저장
        Vote vote = new Vote();
        vote.setTopic(topic);
        vote.setStartDate(startDate);
        vote.setEndDate(endDate);
        voteRepository.save(vote);

        // Option 엔티티 생성 및 저장
        for (String optionText : options) {
            Option option = new Option();
            option.setVote(vote);
            option.setOption(optionText);
            optionRepository.save(option);
        }
    }

    public Vote getVoteById(Long id) {
        return voteRepository.findById(id).orElse(null);
    }
}
