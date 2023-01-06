package com.example.smartorder.common.exception;

import static com.example.smartorder.common.error.ErrorCode.INTERNAL_SERVER_ERROR;

import com.example.smartorder.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
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
	@ExceptionHandler(Exception.class)
	public ApiResponse<String> handleException(Exception e) {
		log.error("Exception is occured", e);

		return ApiResponse.fail(INTERNAL_SERVER_ERROR);
	}
}
