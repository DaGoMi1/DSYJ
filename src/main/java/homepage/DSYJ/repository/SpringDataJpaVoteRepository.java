package homepage.DSYJ.repository;

import homepage.DSYJ.domain.Schedule;
import homepage.DSYJ.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataJpaVoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByStartDateBeforeAndEndDateAfter(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
