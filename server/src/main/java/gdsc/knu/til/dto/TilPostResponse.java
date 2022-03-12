package gdsc.knu.til.dto;

import gdsc.knu.til.domain.TilPost;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TilPostResponse {
	private final Long id;
	private final String title;
	private final LocalDate date;
	private final String content;
	
	// TODO 작성자 정보를 포함해야 함
	
	public TilPostResponse(TilPost tilPost) {
		this.id = tilPost.getId();
		this.title = tilPost.getTitle();
		this.date = tilPost.getDate();
		this.content = tilPost.getContent();
	}
}
