package gdsc.knu.til.controller;

import gdsc.knu.til.dto.TilPostCreateRequest;
import gdsc.knu.til.exception.InvalidParamException;
import gdsc.knu.til.service.TilPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
