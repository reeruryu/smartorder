package com.example.smartorder.common.exception;

import com.example.smartorder.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberException extends RuntimeException {
	private ErrorCode errorCode;
	private String errorMessage;

	public MemberException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getMessage();
	}
}
