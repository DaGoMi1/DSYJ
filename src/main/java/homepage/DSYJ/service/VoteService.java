package homepage.DSYJ.service;

import homepage.DSYJ.repository.SpringDataJpaVoteRepository;

public class VoteService {
    private final SpringDataJpaVoteRepository voteRepository;

    public VoteService(SpringDataJpaVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

}
