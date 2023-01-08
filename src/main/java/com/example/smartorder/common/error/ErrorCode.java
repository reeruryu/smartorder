package com.example.smartorder.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	BAD_REQUEST(400, "잘못된 요청입니다."),
	END_FASTER_THAN_START(400, "시작 시간이 마감 시간보다 더 빨리야 합니다."),


	UNAUTHORIZED(401, "UNAUTHORIZED"),


	// 404 NOT FOUND
	NOT_FOUND_USER(404, "해당하는 유저가 없습니다."),
	NOT_FOUND_STORE(404, "해당하는 가게가 없습니다."),
	NOT_FOUND_CATEGORY(404, "해당하는 카테고리가 없습니다."),
	NOT_FOUND_STOREMENU(404, "해당하는 가게 메뉴가 없습니다."),
	NOT_FOUND_CART(404, "해당하는 장바구니가 없습니다."),
	NOT_FOUND_CARTMENU(404, "해당하는 장바구니 메뉴가 없습니다."),

	CANNOT_BUY_STOREMENU(404, "현재 구매할 수 없는 가게 메뉴입니다"),
	NOT_SAME_STORE(404, "장바구니에 다른 가게 메뉴를 담을 수 없습니다."),

	CANNOT_ACCESS_CART(404, "본인의 장바구니만 수정이 가능합니다."),
	STORE_NOT_OPEN(404, "운영 중인 가게가 아닙니다."),
	CART_EMPTY(404, "장바구니가 비어 있습니다."),
	CANNOT_ACCESS_STORE(404, "매장 점주 아이디가 아닙니다."),



	// 405 Method Not Allowed
	NOT_SUPPORT_REQUEST_METHOD(404, "해당 request method를 지원하지 않습니다."),


	// 415
	NOT_SUPPORT_MEDIA_TYPE(415, "해당 media type을 지원하지 않습니다."),


	INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.")
	;

	private int code;
	private String message;

}
