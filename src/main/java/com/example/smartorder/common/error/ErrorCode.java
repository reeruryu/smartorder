package com.example.smartorder.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	BAD_REQUEST(400, "잘못된 요청입니다."),

	UNAUTHORIZED(401, "UNAUTHORIZED"),

	NOT_FOUND_STORE(403, "해당하는 가게가 없습니다."),
	NOT_FOUND_CATEGORY(403, "해당하는 카테고리가 없습니다."),
	NOT_FOUND_STOREMENU(403, "해당하는 가게 메뉴가 없습니다."),

	INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.")
	;

	private int code;
	private String message;

}
