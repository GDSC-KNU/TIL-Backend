package gdsc.knu.til.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TilPostCreateRequest {
	
	@NotBlank(message = "제목이 비어있습니다.")
	@Size(max=10, message = "최대 길이가 10입니다.")
	private String title;
	
	@NotNull(message = "날짜가 비어있습니다.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	
	@NotBlank(message = "본문이 비어있습니다.")
	@Size(max=10000)
	private String content;
	
	// TODO: author 필드가 필요하다.
}
