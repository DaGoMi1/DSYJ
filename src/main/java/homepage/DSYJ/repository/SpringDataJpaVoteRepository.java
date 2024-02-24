package homepage.DSYJ.repository;

import homepage.DSYJ.domain.Schedule;
import homepage.DSYJ.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataJpaVoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT DISTINCT v.topic FROM Vote v")
    List<String> findDistinctTopics();
}
