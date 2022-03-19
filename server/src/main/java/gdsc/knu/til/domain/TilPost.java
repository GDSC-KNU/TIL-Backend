package gdsc.knu.til.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "til_post")
public class TilPost {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String title;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;

	@Column(length = 10000, nullable = false)
	private String content;
	
	// 작성한 유저 정보
	// @ManyToOne
	// private User클래스 author;

	@Builder
	public TilPost(String title, LocalDate date, String content) {
		this.title = title;
		this.date = date;
		this.content = content;
	}
	
	public void changeInfo(String title, LocalDate date, String content) {
		this.title = title;
		this.date = date;
		this.content = content;
	}
}
