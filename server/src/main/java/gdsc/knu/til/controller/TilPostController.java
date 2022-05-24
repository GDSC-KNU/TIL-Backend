package gdsc.knu.til.controller;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.dto.TilPostSearchRequestDto;
import gdsc.knu.til.exception.InvalidParamException;
import gdsc.knu.til.exception.MissingParamException;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.service.TilPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController()
@RequestMapping("/posts")
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
	@PostMapping("")
	public ResponseEntity<Long> create(
			@Valid @RequestBody TilPostDto.Request requestDto,
			BindingResult bindingResult) {

		// TODO 로그인 정보를 기반으로 동작
		// TODO JWT에서 유저 정보를 추출해내야함.
		long userId = 2L;

		if (bindingResult.hasErrors()) {
			throw new InvalidParamException();
		}

		return ResponseEntity.status(201).body(tilPostService.create(requestDto, userId).getId());
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시글 상세 조회", description = "til 게시글을 상세 조회한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "해당 게시글의 상세 정보를 응답으로 보낸다."),
			@ApiResponse(responseCode = "400", description = "부적절한 파라미터 - id 값은 정수이다."),
			@ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않는다.")
	})
	@GetMapping("/{id}")
	public ResponseEntity<TilPostDto.DetailResponse> findById(@PathVariable Long id) {
		// TODO 로그인 정보를 기반으로 동작
		// TODO JWT에서 유저 정보를 추출해내야함.
		long userId = 1L;

		TilPost tilPost = tilPostService.findByIdOfAuthor(id, userId).orElseThrow(TilPostNotFoundException::new);
		TilPostDto.Info tilPostInfo = new TilPostDto.Info(tilPost);

		return ResponseEntity.ok(new TilPostDto.DetailResponse(tilPostInfo));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시글 목록 조회", description = "로그인된 유저의 게시글 목록을 조회한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "게시글 목록을 응답으로 보낸다.")
	})
	@GetMapping("")
	public ResponseEntity<TilPostDto.ListResponse> findAll(
			@Parameter(description = "게시글을 가져올 달")
			@RequestParam(required = false)
			@DateTimeFormat(pattern = "yyyy-MM")
					Optional<YearMonth> yearMonth
	) {
		// TODO 로그인 정보를 기반으로 동작
		// TODO JWT에서 유저 정보를 추출해내야함.
		long userId = 1L;

		List<TilPost> tilPosts;
		if (yearMonth.isPresent()) {
			tilPosts = tilPostService.findByYearMonth(userId, yearMonth.get());
		} else {
			tilPosts = tilPostService.findAll(userId);
		}

		List<TilPostDto.Info> postDtoInfos = tilPosts.stream().map(TilPostDto.Info::new).collect(Collectors.toList());
		return ResponseEntity.ok(new TilPostDto.ListResponse(postDtoInfos));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시글 검색", description = "로그인된 유저의 게시글 목록을 키워드로 검색한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "검색된 게시글 목록을 응답으로 보낸다.")
	})
	@GetMapping("/search")
	public ResponseEntity<TilPostDto.ListResponse> search(
			@Parameter(description = "검색할 키워드/카테고리")
			@ModelAttribute
			@Valid
					TilPostSearchRequestDto request,
			@PageableDefault(sort = "date", direction = Sort.Direction.DESC)
					Pageable pageable,
			BindingResult bindingResult
	) {
		if (bindingResult.hasErrors()) {
			throw new MissingParamException();
		}
		// TODO 로그인 정보를 기반으로 동작
		// TODO JWT에서 유저 정보를 추출해내야함.
		long userId = 1L;

		List<TilPost> tilPosts = tilPostService.search(userId, request.getQuery(), pageable);

		List<TilPostDto.Info> postDtoInfos = tilPosts.stream().map(TilPostDto.Info::new).collect(Collectors.toList());
		return ResponseEntity.ok(new TilPostDto.ListResponse(postDtoInfos));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시물 수정", description = "해당 til 게시글을 수정한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "해당 게시글의 id를 응답으로 보낸다."),
			@ApiResponse(responseCode = "400", description = "부적절한 파라미터 - id 값은 정수이다."),
			@ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않는다.")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Long> edit(
			@PathVariable Long id,
			@Valid @RequestBody TilPostDto.Request requestDto,
			BindingResult bindingResult) {

		// TODO 로그인 정보를 기반으로 동작

		if (bindingResult.hasErrors()) {
			throw new InvalidParamException();
		}

		return ResponseEntity.ok(tilPostService.edit(id, requestDto));
	}

	@Tag(name = "Til Post")
	@Operation(summary = "til 게시물 삭제", description = "해당 til 게시물을 삭제한다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "해당 게시글을 삭제하였다."),
			@ApiResponse(responseCode = "400", description = "부적절한 파라미터 - id 값은 정수이다."),
			@ApiResponse(responseCode = "404", description = "해당 게시글이 존재하지 않는다.")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		// TODO 로그인 정보를 기반으로 동작

		tilPostService.delete(id);

		return ResponseEntity.ok().build();
	}
}
