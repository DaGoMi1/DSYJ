package homepage.DSYJ.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<String, String> {
    void save(String key, String authCode);
}
