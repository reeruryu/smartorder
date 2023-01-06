package com.example.smartorder.common.dto;

import com.example.smartorder.common.error.ErrorCode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

	private static int SUCCESS = 200;

	private ApiHeader header;
	private ApiBody body;

	public static <T> ApiResponse<T> OK() {
		return new ApiResponse<T>(new ApiHeader(SUCCESS, "SUCCESS")
			,null);
	}

	public static <T> ApiResponse<T> OK(T data) {
		return new ApiResponse<T>(new ApiHeader(SUCCESS, "SUCCESS")
			, new ApiBody(data, null));
	}

	public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
		return new ApiResponse<>(new ApiHeader(errorCode.getCode(),
			errorCode.name()), new ApiBody(null, errorCode.getMessage()));
	}

}
