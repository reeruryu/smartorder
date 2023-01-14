package com.example.smartorder.common.exception;

import static com.example.smartorder.common.error.ErrorCode.BAD_REQUEST;
import static com.example.smartorder.common.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.smartorder.common.error.ErrorCode.NOT_SUPPORT_MEDIA_TYPE;
import static com.example.smartorder.common.error.ErrorCode.NOT_SUPPORT_REQUEST_METHOD;

import com.example.smartorder.common.dto.ApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ApiResponse handleException(CustomException e) {
		log.error("{} is occured", e.getErrorCode());

		return ApiResponse.fail(e.getErrorCode());
	}

	@ExceptionHandler(BindException.class)
	public ApiResponse handleException(BindException e) {
		log.error("{} is occured", BAD_REQUEST.getCode());

		BindingResult bindingResult = e.getBindingResult();
		List<ObjectError> errors = bindingResult.getAllErrors();

		return ApiResponse.fail(errors);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ApiResponse handleException(MissingServletRequestParameterException e) {

		return ApiResponse.fail(e.getMessage());
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ApiResponse handleException(MissingPathVariableException e) {

		return ApiResponse.fail(e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ApiResponse handleException(HttpRequestMethodNotSupportedException e) {
		log.error("{} is occured", NOT_SUPPORT_REQUEST_METHOD.getCode());

		return ApiResponse.fail(NOT_SUPPORT_REQUEST_METHOD);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ApiResponse handleException(HttpMediaTypeNotSupportedException e) {
		log.error("{} is occured", NOT_SUPPORT_MEDIA_TYPE.getCode());

		return ApiResponse.fail(NOT_SUPPORT_MEDIA_TYPE);
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse handleException(Exception e) {
		log.error("Exception is occured", e);

		return ApiResponse.fail(INTERNAL_SERVER_ERROR);
	}
}
