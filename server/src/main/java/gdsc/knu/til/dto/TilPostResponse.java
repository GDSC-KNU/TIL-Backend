package gdsc.knu.til.dto;

import gdsc.knu.til.domain.TilPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Schema(description = "Til 게시물 상세 조회 응답 DTO")
@Getter
public class TilPostResponse {
	
	@Schema(description = "게시물 id")
	private final Long id;
	
	@Schema(description = "게시물 제목")
	private final String title;

	@Schema(description = "게시물 날짜")
	private final LocalDate date;

	@Schema(description = "게시물 본문")
	private final String content;
	
	// TODO 작성자 정보를 포함해야 함
	
	public TilPostResponse(TilPost tilPost) {
		this.id = tilPost.getId();
		this.title = tilPost.getTitle();
		this.date = tilPost.getDate();
		this.content = tilPost.getContent();
	}
}
