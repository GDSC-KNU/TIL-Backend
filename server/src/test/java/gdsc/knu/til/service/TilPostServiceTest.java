package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.repository.TilPostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TilPostServiceTest {

	@InjectMocks
	private TilPostService tilPostService;

	@Mock(lenient = true)
	private TilPostRepository tilPostRepository;
	
	@Test
	@DisplayName("요청을 기반으로 til 게시글을 생성한다.")
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
	@DisplayName("요청된 id에 해당하는 til 게시글을 가져온다.")
	void findByIdIfExists() {
		// given
		Long request = 1L;

		Long post1Id = 1L;
		TilPost tilPost1 = fixtureTilPost(post1Id);
		ReflectionTestUtils.setField(tilPost1, "id", post1Id);

		Long post2Id = 2L;
		TilPost tilPost2 = fixtureTilPost(post2Id);
		ReflectionTestUtils.setField(tilPost2, "id", post2Id);

		given(tilPostRepository.findById(post1Id))
				.willReturn(Optional.of(tilPost1));
		given(tilPostRepository.findById(post2Id))
				.willReturn(Optional.of(tilPost2));

		// when
		Optional<TilPostDto.Info> tilPostInfoOptional = tilPostService.findById(request);

		// then
		assertThat(tilPostInfoOptional).isNotEmpty();

		TilPostDto.Info expectedTilPostInfo = new TilPostDto.Info(tilPost1);
		TilPostDto.Info actualTilPostInfo = tilPostInfoOptional.get();

		assertThat(expectedTilPostInfo.getId()).isEqualTo(actualTilPostInfo.getId());
		assertThat(expectedTilPostInfo.getTitle()).isEqualTo(actualTilPostInfo.getTitle());
		assertThat(expectedTilPostInfo.getDate()).isEqualTo(actualTilPostInfo.getDate());
		assertThat(expectedTilPostInfo.getContent()).isEqualTo(actualTilPostInfo.getContent());
	}

	@Test
	@DisplayName("요청된 id에 해당하는 til 게시글이 없다면, Optional.empty()를 반환한다.")
	void findByIdIfNotExists() {
		// given
		Long request = 2L;

		Long postId = 1L;
		TilPost tilPost = fixtureTilPost();
		ReflectionTestUtils.setField(tilPost, "id", postId);

		given(tilPostRepository.findById(postId))
				.willReturn(Optional.of(tilPost));

		// when
		Optional<TilPostDto.Info> tilPostInfoOptional = tilPostService.findById(request);

		// then
		assertThat(tilPostInfoOptional).isEmpty();
	}

	@Test
	@DisplayName("모든 게시글을 담은 List를 반환한다.")
	void findAllIfExists() {
		// given
		List<TilPost> tilPosts = LongStream
				.range(1, 6)
				.mapToObj(id -> {
					TilPost tilPost = fixtureTilPost(id);
					ReflectionTestUtils.setField(tilPost, "id", id);
					return tilPost;
				})
				.collect(Collectors.toList());

		given(tilPostRepository.findAll())
				.willReturn(tilPosts);

		// when
		List<TilPostDto.Info> tilPostInfos = tilPostService.findAll();

		// then
		List<TilPostDto.Info> expectedTilPostInfos = tilPosts
				.stream()
				.map(TilPostDto.Info::new)
				.collect(Collectors.toList());

		assertThat(tilPostInfos)
				.hasSize(expectedTilPostInfos.size())
				.hasSameElementsAs(expectedTilPostInfos);
	}

	@Test
	@DisplayName("게시글이 없다면, 빈 List를 반환한다.")
	void findAllIfNotExists() {
		// given
		List<TilPost> tilPosts = new ArrayList<>();

		given(tilPostRepository.findAll())
				.willReturn(tilPosts);

		// when
		List<TilPostDto.Info> tilPostInfos = tilPostService.findAll();

		// then
		assertThat(tilPostInfos).isEmpty();
	}

	@Test
	@DisplayName("해당하는 게시글을 수정할 수 있다.")
	void edit() {
		// given
		Long postId = 1L;
		TilPostDto.Request request = fixtureTilPostRequest(
				"update title",
				"update content",
				LocalDate.of(2222, 1, 1)
		);
		TilPost tilPost = fixtureTilPost();
		TilPost updatedPost = fixtureTilPost(
				"update title",
				"update content",
				LocalDate.of(2222, 1, 1)
		);

		ReflectionTestUtils.setField(tilPost, "id", postId);
		ReflectionTestUtils.setField(updatedPost, "id", postId);

		// Mocking
		given(tilPostRepository.save(BDDMockito.eq(updatedPost)))
				.willReturn(updatedPost);
		given(tilPostRepository.findById(postId))
				.willReturn(Optional.of(tilPost))
				.willReturn(Optional.of(updatedPost));

		// when
		Long editedPostId = tilPostService.edit(postId, request);

		// then
		TilPost editedPost = tilPostRepository.findById(editedPostId).orElseThrow();

		assertThat(editedPost.getId()).isEqualTo(updatedPost.getId());
		assertThat(editedPost.getTitle()).isEqualTo(updatedPost.getTitle());
		assertThat(editedPost.getDate()).isEqualTo(updatedPost.getDate());
		assertThat(editedPost.getContent()).isEqualTo(updatedPost.getContent());
	}

	@Test
	@DisplayName("해당하는 게시글이 없다면, TilPostNotFoundException를 Throw한다.")
	void editIfNotExists() {
		// given
		Long postId = 1L;
		TilPostDto.Request request = fixtureTilPostRequest(
				"update title",
				"update content",
				LocalDate.of(2222, 1, 1)
		);

		// Mocking
		given(tilPostRepository.findById(anyLong()))
				.willThrow(TilPostNotFoundException.class);

		// when & then
		assertThrows(TilPostNotFoundException.class, () -> {
			tilPostService.edit(postId, request);
		});
		
		verify(tilPostRepository, times(1)).findById(anyLong());
	}
	
	@Test
	@DisplayName("해당하는 게시글을 삭제한다.")
	void delete() {
		// given
		Long postId = 1L;

		TilPost tilPost = fixtureTilPost();
		ReflectionTestUtils.setField(tilPost, "id", postId);


		// Mocking
		willDoNothing().given(tilPostRepository)
				.delete(BDDMockito.isA(TilPost.class));
		given(tilPostRepository.findById(postId))
				.willReturn(Optional.of(tilPost))
				.willReturn(Optional.empty());

		// when
		tilPostService.delete(postId);


		// then
		Optional<TilPost> deletedPost = tilPostRepository.findById(postId);

		assertThat(deletedPost).isEmpty();
		verify(tilPostRepository, times(1)).delete(BDDMockito.isA(TilPost.class));
	}

	@Test
	@DisplayName("해당하는 게시글이 없다면, TilPostNotFoundException를 Throw한다.")
	void deleteIfNotExists() {
		// given
		Long postId = 1L;
		
		// Mocking
		given(tilPostRepository.findById(postId))
				.willReturn(Optional.empty());

		// when & then
		assertThrows(TilPostNotFoundException.class, () -> {
			tilPostService.delete(postId);
		});

		verify(tilPostRepository, times(1)).findById(anyLong());
	}

	private TilPost fixtureTilPost() {
		return fixtureTilPost(
				"title",
				"content",
				LocalDate.of(2022, 2, 22)
		);
	}

	private TilPost fixtureTilPost(Long id) {
		return fixtureTilPost(
				"title" + id,
				"content" + id,
				LocalDate.of(2022, 2, 22)
		);
	}

	private TilPost fixtureTilPost(String title, String content, LocalDate localDate) {
		return TilPost.builder()
				.title(title)
				.content(content)
				.date(localDate)
				.build();
	}

	private TilPostDto.Request fixtureTilPostRequest() {
		return fixtureTilPostRequest(
				"title",
				"content",
				LocalDate.of(2022, 2, 22)
		);
	}

	private TilPostDto.Request fixtureTilPostRequest(String title, String content, LocalDate localDate) {
		return TilPostDto.Request
				.builder()
				.title(title)
				.content(content)
				.date(localDate)
				.build();
	}
}