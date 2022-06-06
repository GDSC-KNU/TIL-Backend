package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class MissingParamException extends ResponseException {
	public MissingParamException() {
		super(ErrorCode.MISSING_PARAM);
	}
}
