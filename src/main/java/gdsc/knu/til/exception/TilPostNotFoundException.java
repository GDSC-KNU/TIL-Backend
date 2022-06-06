package gdsc.knu.til.exception;

import lombok.Getter;

@Getter
public class TilPostNotFoundException extends ResponseException {
	public TilPostNotFoundException() {
		super(ErrorCode.TIL_POST_NOT_FOUND);
	}
}
