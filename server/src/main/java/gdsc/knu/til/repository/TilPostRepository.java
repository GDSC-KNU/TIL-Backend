package gdsc.knu.til.repository;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TilPostRepository extends JpaRepository<TilPost, Long> {
	
	List<TilPost> findByAuthor(User author);
	
	List<TilPost> findByAuthorAndDateBetween(User author, LocalDate date, LocalDate date2);

	@Query("select p from TilPost p where p.author = :author and (p.title like %:keyword% or p.content like %:keyword%)")
	List<TilPost> searchByKeyword(@Param("author") User author, @Param("keyword") String keyword, Pageable pageable);
}
