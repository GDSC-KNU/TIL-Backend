package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class TilPostNotFoundException extends RuntimeException {
	private final ErrorCode errorCode;
	
	public TilPostNotFoundException() {
		this(ErrorCode.TIL_POST_NOT_FOUND);
	}
	
	public TilPostNotFoundException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
