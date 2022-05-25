package gdsc.knu.til.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@ToString
public class NumberOfPostsOfDay {
	
	private final LocalDate date;
	private final Long number;
}
