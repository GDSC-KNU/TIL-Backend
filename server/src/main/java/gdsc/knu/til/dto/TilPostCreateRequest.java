package gdsc.knu.til.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Til 게시물 생성 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TilPostCreateRequest {
	
	@Schema(description = "게시글 제목", minLength = 1, maxLength = 50, required = true)
	@NotBlank(message = "제목이 비어있습니다.")
	@Size(max=50, message = "최대 길이가 50입니다.")
	private String title;
	
	@Schema(description = "작성하고자 하는 날짜", required = true)
	@NotNull(message = "날짜가 비어있습니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	
	@Schema(description = "게시글 본문", minLength = 1, maxLength = 10000, required = true)
	@NotBlank(message = "본문이 비어있습니다.")
	@Size(max=10000)
	private String content;
	
	// TODO: author 필드가 필요하다.
}
