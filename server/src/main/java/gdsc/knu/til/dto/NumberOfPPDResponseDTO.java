package gdsc.knu.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Schema(description = "로그인된 사용자의 최근 6개월간 게시글 작성 수")
@Getter
@RequiredArgsConstructor
public class NumberOfPPDResponseDTO {
	
	@Schema(description = "날짜와 그 날짜의 게시글 개수를 담고있는 리스트")
	private final List<NumberOfPostsOfDay> data;
}
