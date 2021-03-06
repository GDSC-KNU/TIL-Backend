package gdsc.knu.til.service;

import gdsc.knu.til.domain.TilPost;
import gdsc.knu.til.domain.User;
import gdsc.knu.til.dto.TilPostDto;
import gdsc.knu.til.exception.PostForbiddenException;
import gdsc.knu.til.exception.TilPostNotFoundException;
import gdsc.knu.til.repository.TilPostRepository;
import gdsc.knu.til.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
	
	@Mock(lenient = true)
	private UserRepository userRepository;
	
	private User user;
	
	@BeforeEach
	public void setupUser() {
		user = fixtureUser();
		Long userId = 1L;
		ReflectionTestUtils.setField(user, "id", userId);

		given(userRepository.getById(user.getId())).willReturn(user);
	}
	
	@Nested
	@DisplayName("create")
	class CreateTest {
		@Test
		@DisplayName("요청을 기반으로 til 게시글을 생성한다.")
		void create() {
			// given
			TilPostDto.Request request = fixtureTilPostRequest();

			TilPost expectedPost = fixtureTilPost(user);
			Long postId = 1L;
			ReflectionTestUtils.setField(expectedPost, "id", postId);

			// Mocking
			given(tilPostRepository.save(BDDMockito.isA(TilPost.class)))
					.willAnswer(invocation -> {
						TilPost mockTilPost = (TilPost) invocation.getArguments()[0];
						ReflectionTestUtils.setField(mockTilPost, "id", postId);
						return mockTilPost;
					});


			// when
			TilPost createdPost = tilPostService.create(request, user.getId());

			// then
			assertThat(createdPost).isEqualTo(expectedPost);
		}
		
		@Test
		@DisplayName("인자로 넣어준 유저 아이디에 해당하는 유저가 없다면, 에러를 발생한다.")
		void throwErrorIFNotExistsUser() {
			// given
			TilPostDto.Request request = fixtureTilPostRequest();

			TilPost expectedPost = fixtureTilPost(user);
			Long postId = 1L;
			ReflectionTestUtils.setField(expectedPost, "id", postId);

			// Mocking
			given(tilPostRepository.save(BDDMockito.isA(TilPost.class)))
					.willAnswer(invocation -> {
						TilPost mockTilPost = (TilPost) invocation.getArguments()[0];
						
						if (mockTilPost.getAuthor() != user) {
							throw new DataIntegrityViolationException("user not exists");
						}
						
						ReflectionTestUtils.setField(mockTilPost, "id", postId);
						return mockTilPost;
					});
			
			// when
			Exception exception = assertThrows(DataIntegrityViolationException.class, () -> 
				tilPostService.create(request, user.getId() + 1)
			);

			// then
			assertThat(exception.getMessage()).contains("user not exists");
		}
	}

	@Nested
	@DisplayName("findByIdOfUser")
	class FindByIdTest {
		@Test
		@DisplayName("요청된 id에 해당하는 til 게시글을 가져온다.")
		void returnTilPost() {
			// given
			Long postId = 1L;
			TilPost expectedPost = fixtureTilPost(postId, user);
			ReflectionTestUtils.setField(expectedPost, "id", postId);
			
			given(tilPostRepository.findById(postId))
					.willAnswer(invocation -> {
						Long inputPostId = (Long) invocation.getArguments()[0];
						TilPost mockTilPost = fixtureTilPost(inputPostId, user);
						ReflectionTestUtils.setField(mockTilPost, "id", postId);
						
						return Optional.of(mockTilPost);
					});
			
			// when
			Optional<TilPost> postOptional = tilPostService.findByIdOfAuthor(postId, user.getId());

			// then
			assertThat(postOptional).isNotEmpty();

			TilPost actualPost = postOptional.get();
			assertThat(actualPost).isEqualTo(expectedPost);
		}
		
		@Test
		@DisplayName("요청된 id에 해당하는 til 게시글이 없다면, Optional.empty()를 반환한다.")
		void returnOptionalEmptyIfNotExists() {
			// given
			Long requestPostId = 2L;

			Long postId = 1L;
			TilPost tilPost = fixtureTilPost(user);
			ReflectionTestUtils.setField(tilPost, "id", postId);

			given(tilPostRepository.findById(postId))
					.willReturn(Optional.of(tilPost));

			// when
			Optional<TilPost> postOptional = tilPostService.findByIdOfAuthor(requestPostId, user.getId());

			// then
			assertThat(postOptional).isEmpty();
		}
		
		@Test
		@DisplayName("til 게시글의 author가 인자로 들어온 유저 정보와 다르다면, Optional.empty()를 반환한다.")
		void returnOptionalEmptyIfNotAuth() {
			// given
			Long postId = 1L;
			TilPost tilPost = fixtureTilPost(user);
			ReflectionTestUtils.setField(tilPost, "id", postId);

			given(tilPostRepository.findById(postId))
					.willReturn(Optional.of(tilPost));

			// when
			Optional<TilPost> postOptional = tilPostService.findByIdOfAuthor(postId, user.getId() + 1);

			// then
			assertThat(postOptional).isEmpty();
		}
	}
	
	@Nested
	@DisplayName("findAll")
	class FindAllTest {
		@Test
		@DisplayName("모든 게시글을 담은 List를 반환한다.")
		void returnTilPostList() {
			// given
			List<TilPost> tilPosts = LongStream
					.range(1, 6)
					.mapToObj(id -> {
						TilPost tilPost = fixtureTilPost(id, user);
						ReflectionTestUtils.setField(tilPost, "id", id);
						return tilPost;
					})
					.collect(Collectors.toList());

			given(tilPostRepository.findByAuthor(user))
					.willReturn(tilPosts);

			// when
			List<TilPost> posts = tilPostService.findAll(user.getId());

			// then
			assertThat(posts)
					.hasSize(tilPosts.size())
					.hasSameElementsAs(tilPosts);
		}

		@Test
		@DisplayName("게시글이 없다면, 빈 List를 반환한다.")
		void returnEmptyListIfNotExists() {
			// given
			List<TilPost> tilPosts = new ArrayList<>();

			given(tilPostRepository.findByAuthor(user))
					.willReturn(tilPosts);

			// when
			List<TilPost> posts = tilPostService.findAll(user.getId());

			// then
			assertThat(posts).isEmpty();
		}
	}

	@Nested
	@DisplayName("findByYearMonth")
	class FindByYearMonthTest {
		@Test
		@DisplayName("해당하는 달의 게시글을 반환한다.")
		void returnTilPostList() {
			// given
			List<TilPost> tilPostsOfFeb = LongStream
					.range(1, 6)
					.mapToObj(id -> {
						TilPost tilPost = fixtureTilPost(
								"title" + id,
								"content" + id,
								LocalDate.of(2022, 2, (int) (1 + id)),
								user
						);
						ReflectionTestUtils.setField(tilPost, "id", id);
						return tilPost;
					})
					.collect(Collectors.toList());
			
			List<TilPost> tilPostsOfMar = LongStream
					.range(1, 6)
					.mapToObj(id -> {
						TilPost tilPost = fixtureTilPost(
								"title" + id,
								"content" + id,
								LocalDate.of(2022, 3, (int) (1 + id)),
								user
						);
						ReflectionTestUtils.setField(tilPost, "id", id);
						return tilPost;
					})
					.collect(Collectors.toList());

			YearMonth feb2022 = YearMonth.of(2022, 2);
			YearMonth feb2023 = YearMonth.of(2022, 3);

			given(tilPostRepository.findByAuthorAndDateBetween(
					eq(user),
					BDDMockito.eq(feb2022.atDay(1)),
					BDDMockito.eq(feb2022.atEndOfMonth())
			)).willReturn(tilPostsOfFeb);
			given(tilPostRepository.findByAuthorAndDateBetween(
					eq(user),
					BDDMockito.eq(feb2023.atDay(1)),
					BDDMockito.eq(feb2023.atEndOfMonth())
			)).willReturn(tilPostsOfMar);

			// when
			List<TilPost> postsOfFeb = tilPostService.findByYearMonth(user.getId(), feb2022);
			List<TilPost> postSOfMar = tilPostService.findByYearMonth(user.getId(), feb2023);

			// then
			assertThat(postsOfFeb)
					.hasSize(tilPostsOfFeb.size())
					.hasSameElementsAs(tilPostsOfFeb);
			assertThat(postSOfMar)
					.hasSize(tilPostsOfMar.size())
					.hasSameElementsAs(tilPostsOfMar);
		}

		@Test
		@DisplayName("해당하는 게시글이 없다면, 빈 List를 반환한다.")
		void returnEmptyListIfNotExists() {
			// given
			List<TilPost> tilPosts = new ArrayList<>();

			YearMonth feb2022 = YearMonth.of(2022, 2);

			given(tilPostRepository.findByAuthorAndDateBetween(
					eq(user),
					BDDMockito.eq(feb2022.atDay(1)),
					BDDMockito.eq(feb2022.atEndOfMonth())
			)).willReturn(tilPosts);

			// when
			List<TilPost> posts = tilPostService.findByYearMonth(user.getId(), feb2022);

			// then
			assertThat(posts).isEmpty();
		}
	}

	@Nested
	@DisplayName("edit")
	class EditTest {
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
			TilPost tilPost = fixtureTilPost(user);
			TilPost updatedPost = fixtureTilPost(
					"update title",
					"update content",
					LocalDate.of(2222, 1, 1),
					user
			);

			ReflectionTestUtils.setField(tilPost, "id", postId);
			ReflectionTestUtils.setField(updatedPost, "id", postId);

			// Mocking
			given(tilPostRepository.save(BDDMockito.eq(updatedPost)))
					.willReturn(updatedPost);
			given(tilPostRepository.findById(postId))
					.willReturn(Optional.of(tilPost));

			// when
			Long editedPostId = tilPostService.edit(user.getId(), postId, request);

			// then
			assertThat(editedPostId).isEqualTo(postId);
			
			verify(tilPostRepository, times(1)).save(BDDMockito.eq(updatedPost));
			verify(tilPostRepository, times(1)).findById(anyLong());
		}
		
		@Test
		@DisplayName("게시글과 인자의 author가 다르다면, PostForbiddenException 예외를 throw한다.")
		void throwPostForbiddenExceptionIfNotAuthor() {
			// given
			TilPostDto.Request request = fixtureTilPostRequest(
					"update title",
					"update content",
					LocalDate.of(2222, 1, 1)
			);

			Long postId = 1L;
			TilPost tilPost = fixtureTilPost(user);
			ReflectionTestUtils.setField(tilPost, "id", postId);

			// Mocking
			given(tilPostRepository.findById(postId))
					.willReturn(Optional.of(tilPost));

			// when & then
			assertThrows(PostForbiddenException.class, () -> 
					tilPostService.edit(user.getId() + 1, postId, request));

			verify(tilPostRepository, times(1)).findById(anyLong());
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
			assertThrows(TilPostNotFoundException.class, () -> tilPostService.edit(user.getId(), postId, request));

			verify(tilPostRepository, times(1)).findById(anyLong());
		}
	}

	@Nested
	@DisplayName("delete")
	class DeleteTest {
		@Test
		@DisplayName("해당하는 게시글을 삭제한다.")
		void delete() {
			// given
			Long postId = 1L;

			TilPost tilPost = fixtureTilPost(user);
			ReflectionTestUtils.setField(tilPost, "id", postId);


			// Mocking
			willDoNothing().given(tilPostRepository)
					.delete(BDDMockito.isA(TilPost.class));
			given(tilPostRepository.findById(postId))
					.willReturn(Optional.of(tilPost))
					.willReturn(Optional.empty());

			// when
			tilPostService.delete(user.getId(), postId);


			// then
			Optional<TilPost> deletedPost = tilPostRepository.findById(postId);

			assertThat(deletedPost).isEmpty();
			verify(tilPostRepository, times(1)).delete(BDDMockito.isA(TilPost.class));
		}

		@Test
		@DisplayName("게시글과 인자의 author가 다르다면, PostForbiddenException 예외를 throw한다.")
		void throwPostForbiddenExceptionIfNotAuthor() {
			// given
			Long postId = 1L;
			TilPost tilPost = fixtureTilPost(user);
			ReflectionTestUtils.setField(tilPost, "id", postId);

			// Mocking
			given(tilPostRepository.findById(postId))
					.willReturn(Optional.of(tilPost));

			// when & then
			assertThrows(PostForbiddenException.class, () -> tilPostService.delete(user.getId() + 1, postId));

			verify(tilPostRepository, times(1)).findById(anyLong());
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
			assertThrows(TilPostNotFoundException.class, () -> tilPostService.delete(user.getId(), postId));

			verify(tilPostRepository, times(1)).findById(anyLong());
		}
	}

	private User fixtureUser() {
		return fixtureUser("test123@test.com", "password1234");
	}
	
	private User fixtureUser(String account, String password) {
		return User.builder()
				.account(account)
				.password(password)
				.build();
	}
	
	private TilPost fixtureTilPost(User author) {
		return fixtureTilPost(
				"title",
				"content",
				LocalDate.of(2022, 2, 22),
				author
		);
	}

	private TilPost fixtureTilPost(Long id, User author) {
		return fixtureTilPost(
				"title" + id,
				"content" + id,
				LocalDate.of(2022, 2, 22),
				author
		);
	}

	private TilPost fixtureTilPost(String title, String content, LocalDate localDate, User author) {
		return TilPost.builder()
				.title(title)
				.content(content)
				.date(localDate)
				.author(author)
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