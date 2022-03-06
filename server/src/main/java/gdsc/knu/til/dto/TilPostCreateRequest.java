package gdsc.knu.til.dto;

import gdsc.knu.til.domain.TilPost;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class TilPostCreateRequest {
	private String title;
	private LocalDate date;
	private String content;
	
	// TODO: author 필드와 attribute 필드가 필요하다.
	
	public TilPost toEntity() {
		return TilPost.builder()
				.title(title)
				.date(date)
				.content(content)
				.build();
	}
}
