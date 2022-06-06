package gdsc.knu.til.controller;

import gdsc.knu.til.exception.ErrorCode;
import gdsc.knu.til.exception.ErrorResponse;
import gdsc.knu.til.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyAccessException;
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

	@ExceptionHandler(value = { ResponseException.class })
	public ResponseEntity<ErrorResponse> handleResponseException(ResponseException ex) {
		log.error("handleResponseException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ex.getErrorCode());
	}
	
	@ExceptionHandler(value = { HttpMessageNotReadableException.class, PropertyAccessException.class })
	public ResponseEntity<ErrorResponse> handleConvertRequestException(Exception ex) {
		log.error("handleConvertRequestException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.INVALID_PARAM);
	}

	@ExceptionHandler(value = { MissingRequestValueException.class })
	public ResponseEntity<ErrorResponse> handleMissingRequestValueException(Exception ex) {
		log.error("handleMissingRequestValueException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.MISSING_PARAM);
	}
	
	@ExceptionHandler(value = { BadCredentialsException.class })
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(Exception ex) {
		log.error("handleUnauthorizedException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.WRONG_ACCOUNT_OR_PASSWORD);
	}
	
	@ExceptionHandler(value = { KeyAlreadyExistsException.class })
	public ResponseEntity<ErrorResponse> handleExistsAccount(Exception ex) {
		log.error("handleExistsAccount : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.EXISTS_ACCOUNT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex){
		log.error("handleException : {}", ex.getMessage());
		return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
