package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.dto.TilPostCreateRequest;
import gdsc.knu.til.dto.TilPostResponse;
import gdsc.knu.til.repository.TilPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TilPostService {

	private final TilPostRepository tilPostRepository;

	public TilPostService(TilPostRepository tilPostRepository) {
		this.tilPostRepository = tilPostRepository;
	}

	@Transactional
	public Long create(TilPostCreateRequest requestDto) {
		return tilPostRepository.save(requestDto.toEntity()).getId();
	}
	
	@Transactional(readOnly = true)
	public Optional<TilPostResponse> findById(Long id) {
		Optional<TilPost> tilPostOpt = tilPostRepository.findById(id);
		if (tilPostOpt.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(new TilPostResponse(tilPostOpt.get()));
	}
}
