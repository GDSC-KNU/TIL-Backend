package gdsc.knu.til.controller;

import gdsc.knu.til.exception.ErrorCode;
import gdsc.knu.til.exception.ErrorResponse;
import gdsc.knu.til.exception.InvalidParamException;
import gdsc.knu.til.exception.TilPostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { TilPostNotFoundException.class })
	public ResponseEntity<ErrorResponse> handleTilPostNotFoundException(TilPostNotFoundException ex) {
		log.error("handleTilPostNotFoundException : {}", ex.getErrorCode());
		return ErrorResponse.toResponseEntity(ex.getErrorCode());
	}
	
	@ExceptionHandler(value = { InvalidParamException.class })
	public ResponseEntity<ErrorResponse> handleInvalidParamException(InvalidParamException ex) {
		log.error("handleInvalidParamException : {}", ex.getErrorCode());
		return ErrorResponse.toResponseEntity(ex.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex){
		log.error("handleException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
