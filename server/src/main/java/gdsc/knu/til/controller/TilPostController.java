package gdsc.knu.til.controller;

import gdsc.knu.til.dto.TilPostCreateRequest;
import gdsc.knu.til.dto.TilPostResponse;
import gdsc.knu.til.exception.InvalidParamException;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.service.TilPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class TilPostController {

	private final TilPostService tilPostService;

	public TilPostController(TilPostService tilPostService) {
		this.tilPostService = tilPostService;
	}

	@PostMapping("/til-post")
	public ResponseEntity<Long> create(
			@Valid @RequestBody TilPostCreateRequest requestDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidParamException();
		}

		return ResponseEntity.ok(tilPostService.create(requestDto));
	}

	@GetMapping("/til-post/{id}")
	public ResponseEntity<TilPostResponse> findById(@PathVariable Long id) {
		// TODO 로그인 되어있는 유저의 정보를 가져와서 쿼리를 날려야 함. 
		//  다른 사람껄 들고오면 안되니깐..!
		TilPostResponse tilPost = tilPostService.findById(id).orElseThrow(TilPostNotFoundException::new);

		return ResponseEntity.ok(tilPost);
	}
}
