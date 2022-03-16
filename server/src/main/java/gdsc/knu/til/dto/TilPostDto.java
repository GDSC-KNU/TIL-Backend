package gdsc.knu.til.dto;

import gdsc.knu.til.domain.TilPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class TilPostDto {

	@Schema(description = "Til 게시물 상세 정보 DTO")
	@Getter
	public static class Info {
		@Schema(description = "게시물 id")
		private final Long id;

		@Schema(description = "게시물 제목")
		private final String title;

		@Schema(description = "게시물 날짜")
		private final LocalDate date;

		@Schema(description = "게시물 본문")
		private final String content;

		// TODO 작성자 정보를 포함해야 함. 해야하나?? 필요없지 않나??

		public Info(TilPost tilPost) {
			this.id = tilPost.getId();
			this.title = tilPost.getTitle();
			this.date = tilPost.getDate();
			this.content = tilPost.getContent();
		}
	}

	@Schema(description = "Til 게시물 요청 DTO")
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@ToString
	public static class Request {
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
	}
	
	@Schema(description = "Til 게시물 상세 정보 응답 DTO")
	@Getter
	@AllArgsConstructor
	public static class DetailResponse {
		@Schema(description = "Til 게시물 상세 정보")
		private Info data;
	}
	
	@Schema(description = "Til 게시물 목록 응답 DTO")
	@Getter
	@AllArgsConstructor
	public static class ListResponse {
		@Schema(description = "Til 게시물 목록")
		private List<Info> data;
	}
}
