package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.domain.User;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.repository.TilPostRepository;
import gdsc.knu.til.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TilPostService {

	private final TilPostRepository tilPostRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public TilPost create(TilPostDto.Request requestDto, Long userId) {
		User user = userRepository.getById(userId);
		
		TilPost tilPost = TilPost.builder()
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.date(requestDto.getDate())
				.author(user)
				.build();
		
		return tilPostRepository.save(tilPost);
	}

	@Transactional(readOnly = true)
	public Optional<TilPostDto.Info> findById(Long id) {
		Optional<TilPost> tilPostOpt = tilPostRepository.findById(id);
		if (tilPostOpt.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new TilPostDto.Info(tilPostOpt.get()));
	}
	
	@Transactional(readOnly = true)
	public List<TilPostDto.Info> findAll() {
		List<TilPost> tilPosts = tilPostRepository.findAll();

		return tilPosts.stream().map(TilPostDto.Info::new).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<TilPostDto.Info> findByYearMonth(YearMonth yearMonth) {
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();
		
		List<TilPost> tilPosts = tilPostRepository.findByDateBetween(start, end);
		
		return tilPosts.stream().map(TilPostDto.Info::new).collect(Collectors.toList());
	}
	
	@Transactional
	public Long edit(Long postId, TilPostDto.Request requestDto) throws TilPostNotFoundException {
		TilPost tilPost = tilPostRepository.findById(postId)
				.orElseThrow(TilPostNotFoundException::new);
		
		tilPost.changeInfo(
				requestDto.getTitle(),
				requestDto.getDate(),
				requestDto.getContent()
		);
		
		return tilPostRepository.save(tilPost).getId();
	}
	
	@Transactional
	public void delete(Long postId) throws TilPostNotFoundException {
		TilPost tilPost = tilPostRepository.findById(postId)
				.orElseThrow(TilPostNotFoundException::new);
		
		tilPostRepository.delete(tilPost);
	}
}
