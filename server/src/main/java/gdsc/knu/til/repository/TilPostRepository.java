package gdsc.knu.til.repository;

import gdsc.knu.til.domain.TilPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TilPostRepository extends JpaRepository<TilPost, Long> {
	
	List<TilPost> findByDateBetween(LocalDate start, LocalDate end);
}
