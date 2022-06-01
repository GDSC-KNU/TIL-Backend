package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class NotExistsUserException extends RuntimeException{

	private final ErrorCode errorCode;

	public NotExistsUserException() {
		this(ErrorCode.NOT_EXISTS_USER);
	}

	public NotExistsUserException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
}
