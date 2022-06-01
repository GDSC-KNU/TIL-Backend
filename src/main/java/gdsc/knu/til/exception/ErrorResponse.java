package gdsc.knu.til.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
	private String message;
	private String code;

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
				.status(errorCode.getHttpStatus())
				.body(ErrorResponse.builder()
						.code(errorCode.name())
						.message(errorCode.getMessage())
						.build()
				);
	}
}
