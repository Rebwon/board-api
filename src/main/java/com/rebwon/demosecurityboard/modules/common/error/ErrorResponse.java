package com.rebwon.demosecurityboard.modules.common.error;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.rebwon.demosecurityboard.modules.index.IndexController;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ErrorResponse extends EntityModel<ErrorResponse> {
	private String message;
	private int status;
	private List<FieldError> errors;
	private String code;

	private ErrorResponse(ErrorCode code, List<FieldError> errors) {
		this.message = code.getMessage();
		this.status = code.getStatus();
		this.errors = errors;
		this.code = code.getCode();
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}

	private ErrorResponse(ErrorCode code) {
		this.message = code.getMessage();
		this.status = code.getStatus();
		this.code = code.getCode();
		this.errors = new ArrayList<>();
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}

	public static EntityModel<ErrorResponse> of(ErrorCode code, BindingResult bindingResult) {
		return new ErrorResponse(code, FieldError.of(bindingResult));
	}

	public static EntityModel<ErrorResponse> of(ErrorCode code) {
		return new ErrorResponse(code);
	}

	public static EntityModel<ErrorResponse> of(ErrorCode code, List<FieldError> errors) {
		return new ErrorResponse(code, errors);
	}

	public static EntityModel<ErrorResponse> of(MethodArgumentTypeMismatchException e) {
		final String value = e.getValue() == null ? "" : e.getValue().toString();
		final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value, e.getErrorCode());
		return new ErrorResponse(ErrorCode.INVALID_INPUT, errors);
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class FieldError {
		private String field;
		private String value;
		private String reason;

		public static List<FieldError> of(String field, String value, String reason) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		private static List<FieldError> of(BindingResult bindingResult) {
			final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
				.map(error -> new FieldError(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()))
				.collect(Collectors.toList());
		}
	}
}
