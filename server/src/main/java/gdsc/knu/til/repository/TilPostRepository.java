package gdsc.knu.til.repository;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TilPostRepository extends JpaRepository<TilPost, Long> {
	
	List<TilPost> findByAuthor(User author);
	
	List<TilPost> findByAuthorAndDateBetween(User author, LocalDate date, LocalDate date2);
}
