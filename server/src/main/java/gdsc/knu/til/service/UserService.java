package gdsc.knu.til.service;

import gdsc.knu.til.domain.User;
import gdsc.knu.til.dto.NumberOfPostsPerDay;
import gdsc.knu.til.repository.TilPostRepository;
import gdsc.knu.til.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
	private final TilPostRepository tilPostRepository;
	private final UserRepository userRepository;
	
	public List<NumberOfPostsPerDay> getNumberOfPostsPerDay(Long userId, LocalDate start, LocalDate end) {
		User user = userRepository.getById(userId);

		List<List<Object>> counts = tilPostRepository.countPostPerDate(user, start, end);

		List<NumberOfPostsPerDay> numberOfPPDs = counts.stream()
				.map(obj -> new NumberOfPostsPerDay((LocalDate) obj.get(0), (Long) obj.get(1)))
				.collect(Collectors.toList());

		return start.datesUntil(end).map(date -> 
				numberOfPPDs.stream()
					.filter(obj -> date.equals(obj.getDate()))
					.findAny()
					.orElse(new NumberOfPostsPerDay(date, 0L))
		).collect(Collectors.toList());
	}
}
