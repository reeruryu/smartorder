package com.example.smartorder.common.exception;

import static com.example.smartorder.common.error.ErrorCode.BAD_REQUEST;
import static com.example.smartorder.common.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.smartorder.common.error.ErrorCode.NOT_SUPPORT_MEDIA_TYPE;
import static com.example.smartorder.common.error.ErrorCode.NOT_SUPPORT_REQUEST_METHOD;

import com.example.smartorder.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ApiResponse<String> handleException(NotFoundException e) {
		log.error("{} is occured", e.getErrorCode());

		return ApiResponse.fail(e.getErrorCode());
	}

	@ExceptionHandler(MemberException.class)
	public ApiResponse<String> handleException(MemberException e) {
		log.error("{} is occured", e.getErrorCode());

		return ApiResponse.fail(e.getErrorCode());
	}

	@ExceptionHandler(ValidationException.class)
	public ApiResponse<String> handleException(ValidationException e) {
		log.error("{} is occured", e.getErrorCode());

		return ApiResponse.fail(e.getErrorCode());
	}

	// 405 변경 아직 안함
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResponse<String> handleException(MethodArgumentNotValidException e) {
		log.error("{} is occured", BAD_REQUEST.getCode());

		return ApiResponse.fail(BAD_REQUEST);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ApiResponse<String> handleException(MissingServletRequestParameterException e) {
		log.error("{} is occured", BAD_REQUEST.getCode());

		return ApiResponse.fail(BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ApiResponse<String> handleException(HttpMessageNotReadableException e) {
		log.error("{} is occured", BAD_REQUEST.getCode());

		return ApiResponse.fail(BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ApiResponse<String> handleException(HttpRequestMethodNotSupportedException e) {
		log.error("{} is occured", NOT_SUPPORT_REQUEST_METHOD.getCode());

		return ApiResponse.fail(NOT_SUPPORT_REQUEST_METHOD);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ApiResponse<String> handleException(HttpMediaTypeNotSupportedException e) {
		log.error("{} is occured", NOT_SUPPORT_MEDIA_TYPE.getCode());

		return ApiResponse.fail(NOT_SUPPORT_MEDIA_TYPE);
	}

	@ExceptionHandler(Exception.class)
	public ApiResponse<String> handleException(Exception e) {
		log.error("Exception is occured", e);

		return ApiResponse.fail(INTERNAL_SERVER_ERROR);
	}
}
