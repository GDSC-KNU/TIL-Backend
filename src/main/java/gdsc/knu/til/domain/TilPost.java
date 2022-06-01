package gdsc.knu.til.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "til_posts")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User author;

	@Builder
	public TilPost(String title, LocalDate date, String content, User author) {
		this.title = title;
		this.date = date;
		this.content = content;
		this.author = author;
	}
	
	public void changeInfo(String title, LocalDate date, String content) {
		this.title = title;
		this.date = date;
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TilPost tilPost = (TilPost) o;
		return Objects.equals(id, tilPost.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
