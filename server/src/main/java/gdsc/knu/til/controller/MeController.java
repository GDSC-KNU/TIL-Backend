package gdsc.knu.til.controller;


import gdsc.knu.til.dto.NumberOfPPDResponseDTO;
import gdsc.knu.til.dto.NumberOfPostsPerDay;
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
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "게시글을 정상적으로 만듬. 해당 게시글의 id를 응답으로 보낸다."),
			@ApiResponse(responseCode = "400", description = "정상적이지 않은 요청 파라미터")
	})
	@GetMapping("/number_of_posts_per_day")
	public ResponseEntity<NumberOfPPDResponseDTO> numberOfPostsPerDay() {
		long userId = 1L;

		LocalDate end = LocalDate.now();
		LocalDate start = end.minusMonths(6);

		System.out.println("start = " + start);

		List<NumberOfPostsPerDay> numberOfPostsPerDay = userService.getNumberOfPostsPerDay(userId, start, end);
		
		return ResponseEntity.ok(new NumberOfPPDResponseDTO(numberOfPostsPerDay));
	}
}
