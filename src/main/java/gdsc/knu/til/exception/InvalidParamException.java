package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class InvalidParamException extends ResponseException {
	public InvalidParamException() {
		super(ErrorCode.INVALID_PARAM);
	}
}
