package gdsc.knu.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Schema(description = "최근 6개월간 가장 많은 게시글을 작성한 날짜와 그때의 게시글 개수")
@Getter
@RequiredArgsConstructor
public class MaxPostNumAndDateResDTO {
	@Schema(description = "날짜와 그 날짜의 게시글 개수")
	private final NumberOfPostsOfDay data;
}
