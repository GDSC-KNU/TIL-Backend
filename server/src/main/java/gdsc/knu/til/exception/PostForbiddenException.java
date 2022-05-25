package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class PostForbiddenException extends RuntimeException {
	private final ErrorCode errorCode;

	public PostForbiddenException() {
		this(ErrorCode.POST_FORBIDDEN);
	}
	
	public PostForbiddenException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
