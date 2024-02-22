package homepage.DSYJ.repository;

import homepage.DSYJ.domain.Schedule;
import homepage.DSYJ.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaVoteRepository extends JpaRepository<Vote, Long> {
}
