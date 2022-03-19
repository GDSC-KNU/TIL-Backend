package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.repository.TilPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TilPostService {

	private final TilPostRepository tilPostRepository;

	public TilPostService(TilPostRepository tilPostRepository) {
		this.tilPostRepository = tilPostRepository;
	}

	@Transactional
	public Long create(TilPostDto.Request requestDto) {
		TilPost tilPost = TilPost.builder()
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.date(requestDto.getDate())
				.build();
		
		return tilPostRepository.save(tilPost).getId();
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
	
	@Transactional
	public Optional<Long> edit(Long postId, TilPostDto.Request requestDto) {
		Optional<TilPost> tilPostOptional = tilPostRepository.findById(postId);
		if (tilPostOptional.isEmpty()) {
			return Optional.empty();
		}
		TilPost tilPost = tilPostOptional.get();
		
		tilPost.changeInfo(
				requestDto.getTitle(),
				requestDto.getDate(),
				requestDto.getContent()
		);
		
		return Optional.of(tilPostRepository.save(tilPost).getId());
	}
	
	@Transactional
	public boolean delete(Long postId) {
		Optional<TilPost> tilPost = tilPostRepository.findById(postId);
		if (tilPost.isEmpty()) {
			return false;
		}
		
		tilPostRepository.delete(tilPost.get());
		return true;
	}
}
