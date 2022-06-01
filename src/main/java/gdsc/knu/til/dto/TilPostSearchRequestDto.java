package gdsc.knu.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "Til 게시물 검색 요청 모델")
@Getter
@RequiredArgsConstructor
public class TilPostSearchRequestDto {
	
	@Schema(description = "검색할 키워드", example = "GDSC")
	@NotBlank(message = "검색할 키워드가 비어있습니다.")
	private final String query;
	
}
