package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class NotExistsUserException extends ResponseException {
	public NotExistsUserException() {
		super(ErrorCode.NOT_EXISTS_USER);
	}
}
