package gdsc.knu.til.controller;

import gdsc.knu.til.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.openmbean.KeyAlreadyExistsException;

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
	
	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	public ResponseEntity<ErrorResponse> handleConvertRequestException(HttpMessageNotReadableException ex) {
		log.error("handleConvertRequestException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.INVALID_PARAM);
	}

	@ExceptionHandler(value = { MissingRequestValueException.class, MissingParamException.class })
	public ResponseEntity<ErrorResponse> handleConvertRequestException(Exception ex) {
		log.error("MissingRequestValueException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.MISSING_PARAM);
	}
	
	@ExceptionHandler(value = { PostForbiddenException.class })
	public ResponseEntity<ErrorResponse> handleConvertRequestException(PostForbiddenException ex) {
		log.error("MissingRequestValueException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.POST_FORBIDDEN);
	}
	
	@ExceptionHandler(value = { BadCredentialsException.class })
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exception ex) {
		log.error("MissingRequestValueException : {}", ErrorCode.WRONG_ACCOUNT_OR_PASSWORD.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.WRONG_ACCOUNT_OR_PASSWORD);
	}
	
	@ExceptionHandler(value = { KeyAlreadyExistsException.class })
	public ResponseEntity<ErrorResponse> handleExistsAccount(Exception ex) {
		log.error("MissingRequestValueException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.EXISTS_ACCOUNT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex){
		log.error("handleException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
