package homepage.DSYJ.repository;


import homepage.DSYJ.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaScheduleRepository extends JpaRepository<Schedule, Long> {
}
