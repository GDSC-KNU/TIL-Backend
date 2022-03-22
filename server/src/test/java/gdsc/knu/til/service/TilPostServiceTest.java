package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.repository.TilPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TilPostServiceTest {
	
	@InjectMocks
	private TilPostService tilPostService;
	
	@Mock
	private TilPostRepository tilPostRepository;

	@Test
	void create() {
		// given
		TilPostDto.Request request = fixtureTilPostRequest();
		TilPost tilPost = fixtureTilPost();
		
		Long postId = 1L;
		ReflectionTestUtils.setField(tilPost, "id", postId);
		
		// Mocking
		given(tilPostRepository.save(BDDMockito.isA(TilPost.class)))
				.willReturn(tilPost);
		given(tilPostRepository.findById(postId))
				.willReturn(Optional.of(tilPost));
		
		// when
		Long createdPostId = tilPostService.create(request);
		
		// then
		TilPost createdPost = tilPostRepository.findById(createdPostId).orElseThrow();

		assertThat(createdPost.getId()).isEqualTo(tilPost.getId());
		assertThat(createdPost.getTitle()).isEqualTo(tilPost.getTitle());
		assertThat(createdPost.getDate()).isEqualTo(tilPost.getDate());
		assertThat(createdPost.getContent()).isEqualTo(tilPost.getContent());
	}

	@Test
	void findById() {
	}

	@Test
	void findAll() {
	}

	@Test
	void edit() {
	}

	@Test
	void delete() {
	}

	private TilPost fixtureTilPost() {
		return TilPost.builder()
				.title("title")
				.content("content")
				.date(LocalDate.of(2022, 2, 22))
				.build();
	}
	
	private TilPostDto.Request fixtureTilPostRequest() {
		return TilPostDto.Request
				.builder()
				.title("title")
				.content("content")
				.date(LocalDate.of(2022, 2, 22))
				.build();
	}
}