package homepage.DSYJ.repository;


import homepage.DSYJ.domain.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaPostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findByBoardType(String boardType);
}
