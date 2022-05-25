package gdsc.knu.til.controller;


import gdsc.knu.til.dto.MaxPostNumAndDateResDTO;
import gdsc.knu.til.dto.NumberOfPPDResponseDTO;
import gdsc.knu.til.dto.NumberOfPostsOfDay;
import gdsc.knu.til.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/me")
@Tag(name = "User", description = "사용자 API")
public class MeController {

	private final UserService userService;

	public MeController(UserService userService) {
		this.userService = userService;
	}

	@Tag(name = "User")
	@Operation(summary = "로그인된 사용자의 최근 6개월간 게시글 작성 수", 
			description = "현재 로그인된 사용자의 최근 6개월 동안의 각 날짜별 게시글 작성수를 반환한다.")
	@ApiResponses({ @ApiResponse(responseCode = "200") })
	@GetMapping("/number_of_posts_per_day")
	public ResponseEntity<NumberOfPPDResponseDTO> numberOfPostsPerDay() {
		long userId = 1L;

		LocalDate end = LocalDate.now();
		LocalDate start = end.minusMonths(6);

		List<NumberOfPostsOfDay> numberOfPostsPerDay = userService.getNumberOfPostsPerDay(userId, start, end);
		
		return ResponseEntity.ok(new NumberOfPPDResponseDTO(numberOfPostsPerDay));
	}

	@Tag(name = "User")
	@Operation(summary = "최근 6개월간 게시글을 가장 많이 작성한 날과 게시글 수를 반환한다.",
			description = "현재 로그인된 사용자의 최근 6개월 동안 게시글을 가장 많이 작성한 날과 그 날의 게시글 개수를 반환한다.")
	@ApiResponses({ 
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "작성한 게시글이 전혀 없는 경우, 404 상태코드를 응답한다.")
	})
	@GetMapping("/maximum_post_number_date")
	public ResponseEntity<MaxPostNumAndDateResDTO> maximumPostNumberDate() {
		long userId = 1L;

		LocalDate end = LocalDate.now();
		LocalDate start = end.minusMonths(6);

		Optional<NumberOfPostsOfDay> numberOfPPD = userService.getMaximumPostNumberDate(userId, start, end);

		if (numberOfPPD.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(new MaxPostNumAndDateResDTO(numberOfPPD.get()));
	}
}
