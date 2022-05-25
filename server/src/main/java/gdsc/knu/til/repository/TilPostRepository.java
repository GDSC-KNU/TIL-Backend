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

	List<TilPost> findByAuthorAndDateBetween(User author, LocalDate start, LocalDate end);
	List<TilPost> findByAuthorAndDateBetweenOrderByDate(User author, LocalDate start, LocalDate end);

	@Query("select p.date, count(p) from TilPost p" + 
			" where p.author = :author and p.date between :start and :end" + 
			" group by p.date order by p.date")
	List<List<Object>> countPostPerDate(@Param("author") User author, @Param("start")LocalDate start, @Param("end") LocalDate end);
	
	@Query("select p from TilPost p where p.author = :author and (p.title like %:keyword% or p.content like %:keyword%)")
	List<TilPost> searchByKeyword(@Param("author") User author, @Param("keyword") String keyword, Pageable pageable);
}
