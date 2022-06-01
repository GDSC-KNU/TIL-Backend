package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class InvalidParamException extends RuntimeException {
	private final ErrorCode errorCode;

	public InvalidParamException() {
		this(ErrorCode.INVALID_PARAM);
	}

	public InvalidParamException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
