package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class MissingParamException extends RuntimeException {
	private final ErrorCode errorCode;

	public MissingParamException() {
		this(ErrorCode.MISSING_PARAM);
	}

	public MissingParamException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
