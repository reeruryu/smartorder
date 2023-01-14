package com.example.smartorder.common.dto;

import static com.example.smartorder.common.error.ErrorCode.BAD_REQUEST;

import com.example.smartorder.common.error.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.validation.ObjectError;

@Getter
public class ApiResponse<T> {

	private static int SUCCESS = 200;

	private ApiHeader header;
	private ApiBody body;

	public ApiResponse(ApiHeader header) {
		this.header = header;
	}

	public ApiResponse(ApiHeader header, ApiBody body) {
		this.header = header;
		this.body = body;
	}


	// OK - 반환할 데이터가 없는 응답
	public static ApiResponse OK() {

		return new ApiResponse(new ApiHeader(SUCCESS, "SUCCESS"));
	}

	// OK - 반환할 데이터가 있는 응답
	public static <T> ApiResponse<T> OK(T data) {
		return new ApiResponse<>(new ApiHeader(SUCCESS, "SUCCESS")
			, new ApiBody(data, null));
	}

	// fail - 에러 코드 메세지 반환
	public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
		return new ApiResponse<>(new ApiHeader(errorCode.getCode(),
			errorCode.name()), new ApiBody(null, errorCode.getMessage()));
	}

	// fail - validation 메세지 반환
	public static <T> ApiResponse<T> fail(List<ObjectError> errors) {

		List<String> errorMessage = errors.stream()
			.map(e -> e.getDefaultMessage())
			.collect(Collectors.toList());

		return new ApiResponse<>(new ApiHeader(BAD_REQUEST.getCode(), "BAD_REQUEST"),
			new ApiBody(null, errorMessage));
	}

}
