package homepage.DSYJ.service;

import homepage.DSYJ.domain.Vote;
import homepage.DSYJ.repository.SpringDataJpaVoteRepository;

import java.time.LocalDateTime;
import java.util.List;

public class VoteService {
    private final SpringDataJpaVoteRepository voteRepository;

    public VoteService(SpringDataJpaVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void saveVote(String topic, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Vote vote = new Vote();
        vote.setTopic(topic);
        vote.setStartDate(startDateTime);
        vote.setEndDate(endDateTime);
        voteRepository.save(vote);
    }

    public List<Vote> findVotes() {
        LocalDateTime currentTime = LocalDateTime.now();
        return voteRepository.findByStartDateBeforeAndEndDateAfter(currentTime, currentTime);
    }

}
