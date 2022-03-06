package gdsc.knu.til.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	/* 400 BAD_REQUEST */
	INVALID_PARAM(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터입니다."),
	
	/* 404 NOT_FOUND */
	TIL_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Til 게시글을 찾을 수 없습니다."),
	
	/* 5xx INTER_SERVER_ERROR */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
	;


	private final HttpStatus httpStatus;
	private final String message;
}
