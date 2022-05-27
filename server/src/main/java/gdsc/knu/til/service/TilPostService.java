package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.domain.User;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.exception.PostForbiddenException;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.repository.TilPostRepository;
import gdsc.knu.til.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TilPostService {

	private final TilPostRepository tilPostRepository;
	private final UserRepository userRepository;

	@Transactional
	public TilPost create(TilPostDto.Request requestDto, Long authorId) throws DataIntegrityViolationException {
		User user = userRepository.getById(authorId);

		TilPost tilPost = TilPost.builder()
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.date(requestDto.getDate())
				.author(user)
				.build();

		return tilPostRepository.save(tilPost);
	}

	@Transactional(readOnly = true)
	public Optional<TilPost> findByIdOfAuthor(Long id, Long authorId) {
		Optional<TilPost> tilPostOpt = tilPostRepository.findById(id);
		if (tilPostOpt.isEmpty()) {
			return Optional.empty();
		}

		TilPost tilPost = tilPostOpt.get();
		if (!Objects.equals(tilPost.getAuthor().getId(), authorId)) {
			return Optional.empty();
		}

		return Optional.of(tilPost);
	}

	@Transactional(readOnly = true)
	public List<TilPost> findAll(Long authorId) {
		User author = userRepository.getById(authorId);

		return tilPostRepository.findByAuthor(author);
	}

	@Transactional(readOnly = true)
	public List<TilPost> findByYearMonth(Long authorId, YearMonth yearMonth) {
		User author = userRepository.getById(authorId);

		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();

		return tilPostRepository.findByAuthorAndDateBetween(author, start, end);
	}

	@Transactional(readOnly = true)
	public List<TilPost> search(Long authorId, String keyword, Pageable pageable) {
		User author = userRepository.getById(authorId);

		return tilPostRepository.searchByKeyword(author, keyword, pageable);
	}

	@Transactional
	public Long edit(Long authorId, Long postId, TilPostDto.Request requestDto) throws TilPostNotFoundException, PostForbiddenException {
		TilPost tilPost = tilPostRepository.findById(postId)
				.orElseThrow(TilPostNotFoundException::new);
		
		if (tilPost.getAuthor().getId() != authorId) {
			throw new PostForbiddenException();
		}

		tilPost.changeInfo(
				requestDto.getTitle(),
				requestDto.getDate(),
				requestDto.getContent()
		);

		return tilPostRepository.save(tilPost).getId();
	}

	@Transactional
	public void delete(Long authorId, Long postId) throws TilPostNotFoundException, PostForbiddenException {
		TilPost tilPost = tilPostRepository.findById(postId)
				.orElseThrow(TilPostNotFoundException::new);

		if (tilPost.getAuthor().getId() != authorId) {
			throw new PostForbiddenException();
		}
		
		tilPostRepository.delete(tilPost);
	}
}
