package gdsc.knu.til.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "til_post")
public class TilPost {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="til_post_id")
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
	
	// 속성 정보
	// @ManyToMany
	// private Set<Attribute> attribute;

	@Builder
	public TilPost(String title, LocalDate date, String content) {
		this.title = title;
		this.date = date;
		this.content = content;
	}
}
