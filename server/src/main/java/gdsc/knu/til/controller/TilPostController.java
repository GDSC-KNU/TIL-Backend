package gdsc.knu.til.controller;

import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.exception.InvalidParamException;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.service.TilPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@Tag(name = "Til Post", description = "Til 게시물 API")
public class TilPostController {

	private final TilPostService tilPostService;

	public TilPostController(TilPostService tilPostService) {
		this.tilPostService = tilPostService;
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시글 생성", description = "til 게시글을 생성한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "게시글을 정상적으로 만듬. 해당 게시글의 id를 응답으로 보낸다."),
			@ApiResponse(responseCode = "400", description = "정상적이지 않은 요청 파라미터")
	})
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/til-post")
	public ResponseEntity<Long> create(
			@Valid @RequestBody TilPostDto.Request requestDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidParamException();
		}

		return ResponseEntity.status(201).body(tilPostService.create(requestDto));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시글 상세 조회", description = "til 게시글을 상세 조회한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "해당 게시글의 상세 정보를 응답으로 보낸다."),
			@ApiResponse(responseCode = "400", description = "부적절한 파라미터 - id 값은 정수이다."),
			@ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않는다.")
	})
	@GetMapping("/til-post/{id}")
	public ResponseEntity<TilPostDto.DetailResponse> findById(@PathVariable Long id) {
		// TODO 로그인 되어있는 유저의 정보를 가져와서 쿼리를 날려야 함. 
		//  다른 사람껄 들고오면 안되니깐..!
		TilPostDto.Info tilPost = tilPostService.findById(id).orElseThrow(TilPostNotFoundException::new);

		return ResponseEntity.ok(new TilPostDto.DetailResponse(tilPost));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시글 목록 조회", description = "로그인된 유저의 게시글 목록을 조회한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "게시글 목록을 응답으로 보낸다.")
	})
	@GetMapping("/til-post")
	public ResponseEntity<TilPostDto.ListResponse> findAll() {
		List<TilPostDto.Info> tilPosts = tilPostService.findAll();
		
		return ResponseEntity.ok(new TilPostDto.ListResponse(tilPosts));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시물 수정", description = "해당 til 게시글을 수정한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "해당 게시글의 id를 응답으로 보낸다."),
			@ApiResponse(responseCode = "400", description = "부적절한 파라미터 - id 값은 정수이다."),
			@ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않는다.")
	})
	@PutMapping("/til-post/{id}")
	public ResponseEntity<Long> edit(
			@PathVariable Long id,
			@Valid @RequestBody TilPostDto.Request requestDto,
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			throw new InvalidParamException();
		}

		Long editedPostId = tilPostService.edit(id, requestDto).orElseThrow(TilPostNotFoundException::new);

		return ResponseEntity.ok(editedPostId);
	}
	
	@Tag(name = "Til Post")
	@Operation(summary = "til 게시물 삭제", description = "해당 til 게시물을 삭제한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "해당 게시글을 삭제하였다."),
			@ApiResponse(responseCode = "400", description = "부적절한 파라미터 - id 값은 정수이다."),
			@ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않는다.")
	})
	@DeleteMapping("/til-post/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (tilPostService.delete(id)) {
			return ResponseEntity.ok().build();
		}
		else {
			throw new TilPostNotFoundException();
		}
	}
}
