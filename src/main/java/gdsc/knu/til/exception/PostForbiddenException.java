package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class PostForbiddenException extends ResponseException {
	public PostForbiddenException() {
		super(ErrorCode.POST_FORBIDDEN);
	}
}
