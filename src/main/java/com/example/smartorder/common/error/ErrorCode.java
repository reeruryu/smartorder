package com.example.smartorder.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	BAD_REQUEST(400, "잘못된 요청입니다."),
	END_FASTER_THAN_START(400, "시작 시간이 마감 시간보다 더 빨리야 합니다."),
	USER_NOT_CEO(400, "점주 아이디가 아닙니다."),


	UNAUTHORIZED(401, "UNAUTHORIZED"),
	USER_NOT_EMAIL_AUTH(401, "이메일 인증이 완료되지 않았습니다."),
	USER_STATUS_STOP(401, "정지된 회원입니다."),
	USER_STATUS_WITHDRAW(401, "탈퇴한 회원입니다."),

	// 404 NOT FOUND
	NOT_FOUND_USER(404, "회원 정보가 존재하지 않습니다."),
	NOT_FOUND_STORE(404, "해당하는 가게가 없습니다."),
	NOT_FOUND_CATEGORY(404, "해당하는 카테고리가 없습니다."),
	NOT_FOUND_MENU(404, "해당하는 메뉴가 없습니다."),
	NOT_FOUND_STOREMENU(404, "해당하는 가게 메뉴가 없습니다."),
	NOT_FOUND_CART(404, "해당하는 장바구니가 없습니다."),
	NOT_FOUND_CARTMENU(404, "해당하는 장바구니 메뉴가 없습니다."),
	NOT_FOUND_ORDER(404, "해당하는 주문이 없습니다."),

	CANNOT_BUY_STOREMENU(404, "현재 구매할 수 없는 가게 메뉴입니다"),
	NOT_SAME_STORE(404, "장바구니에 다른 가게 메뉴를 담을 수 없습니다."),

	CANNOT_ACCESS_CART(404, "본인의 장바구니만 수정이 가능합니다."),
	STORE_NOT_OPEN(404, "운영 중인 가게가 아닙니다."),
	CART_EMPTY(404, "장바구니가 비어 있습니다."),
	CANNOT_ACCESS_STORE(404, "매장 점주 아이디가 아닙니다."),
	CANNOT_ACCESS_ORDER(404, "본인(혹은 점주)의 주문만 접근이 가능합니다."),
	CANNOT_CANCEL_ORDER(404, "주문을 취소할 수 없습니다."),
	ORDER_ALREADY_CANCEL(404, "이미 취소된 주문입니다."),




	// 405 Method Not Allowed
	NOT_SUPPORT_REQUEST_METHOD(404, "해당 request method를 지원하지 않습니다."),


	// 409
	ALREADY_USERID_EXISTS(409, "이미 사용 중인 아이디입니다."),
	ALREADY_AUTH_EMAIL(409, "이미 이메일 인증을 완료한 계정입니다."),
	ALREADY_CATEGORY_NAME_EXISTS(409, "중복된 카테고리명입니다."),
	ALREADY_MENU_NAME_EXISTS(409, "중복된 메뉴명입니다."),
	ALREADY_STORE_NAME_EXISTS(409, "중복된 가게명입니다."),



	// 415
	NOT_SUPPORT_MEDIA_TYPE(415, "해당 media type을 지원하지 않습니다."),


	// 500
	INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
	FAIL_SEND_MAIL(500, "메일 전송에 실패했습니다.")
	;

	private int code;
	private String message;

}
