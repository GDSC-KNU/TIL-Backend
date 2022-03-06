package gdsc.knu.til.service;

import gdsc.knu.til.dto.TilPostCreateRequest;
import gdsc.knu.til.repository.TilPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
