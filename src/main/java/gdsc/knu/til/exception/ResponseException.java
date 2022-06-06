package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {
	private final ErrorCode errorCode;

	public ResponseException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
