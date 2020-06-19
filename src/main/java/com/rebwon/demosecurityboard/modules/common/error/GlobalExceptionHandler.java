package com.rebwon.demosecurityboard.modules.common.error;

import java.nio.file.AccessDeniedException;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rebwon.demosecurityboard.modules.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<EntityModel<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException: ", e);
		EntityModel<ErrorResponse> responseModel = ErrorResponse.of(ErrorCode.INVALID_INPUT, e.getBindingResult());
		return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<EntityModel<ErrorResponse>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("HttpRequestMethodNotSupportedException", e);
		EntityModel<ErrorResponse> responseModel = ErrorResponse.of(ErrorCode.METHOD_NOW_ALLOWED);
		return new ResponseEntity<>(responseModel, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<EntityModel<ErrorResponse>> handleAccessDeniedException(AccessDeniedException e) {
		log.error("AccessDeniedException", e);
		EntityModel<ErrorResponse> responseModel = ErrorResponse.of(ErrorCode.FORBIDDEN);
		return new ResponseEntity<>(responseModel, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<EntityModel<ErrorResponse>> handleBusinessException(BusinessException e) {
		log.error("BusinessException", e);
		EntityModel<ErrorResponse> responseModel = ErrorResponse.of(e.getErrorCode());
		return new ResponseEntity<>(responseModel, HttpStatus.valueOf(e.getErrorCode().getStatus()));
	}
}
