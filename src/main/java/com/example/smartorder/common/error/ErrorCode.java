package com.example.smartorder.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	// 400 잘못된 요청
	BAD_REQUEST(400, "잘못된 요청입니다."),
	END_FASTER_THAN_START(400, "시작 시간이 마감 시간보다 더 빨리야 합니다."),
	CANNOT_BUY_STOREMENU(400, "현재 구매할 수 없는 가게 메뉴입니다"),
	NOT_SAME_STORE(400, "장바구니에 다른 가게 메뉴를 담을 수 없습니다."),
	STORE_NOT_OPEN(400, "운영 중인 가게가 아닙니다."),
	CART_EMPTY(400, "장바구니가 비어 있습니다."),
	CANNOT_CANCEL_ORDER(400, "주문을 취소할 수 없습니다."),
	INVALID_ADDRESS(400, "잘못된 주소입니다."),
	CANNOT_CHANGE_CANCELED_ORDER(400, "취소된 주문입니다."),
	NOT_TODAY_ORDER(400, "오늘(현재) 주문이 아닙니다."),
	CANNOT_CHANGE_COMPLETE_ORDER_STATE(400, "완료된 주문상태는 바꿀 수 없습니다"),
	CANNOT_CHANGE_SAME_ORDER_STATE(400, "같은 주문상태로 바꿀 수 없습니다."),
	CANNOT_CHANGE_PREVIOUS_ORDER_STATE(400, "전 주문 상태로 바꿀 수 없습니다."),
	NOT_ENOUGH_BALANCE(400, "잔액이 부족합니다."),
	INVALID_PAYMENT_REQUEST(400, "잘못된 결제 요청입니다"),


	// 401 인증 정보 없음
	UNAUTHORIZED(401, "UNAUTHORIZED"),


	// 403 접근 권한 없음
	USER_NOT_EMAIL_AUTH(403, "이메일 인증이 완료되지 않았습니다."),
	USER_STATUS_STOP(403, "정지된 회원입니다."),
	USER_STATUS_WITHDRAW(403, "탈퇴한 회원입니다."),
	USER_NOT_CEO(403, "점주 권한을 가진 아이디가 아닙니다."),
	CANNOT_ACCESS_STORE(403, "해당 매장 점주 아이디가 아닙니다."),
	CANNOT_ACCESS_ORDER(403, "본인(혹은 점주)의 주문만 접근이 가능합니다."),
	CANNOT_ACCESS_CART(403, "본인의 장바구니만 수정이 가능합니다."),


	// 404 찾을 수 없음
	NOT_FOUND_USER(404, "회원 정보가 존재하지 않습니다."),
	NOT_FOUND_STORE(404, "해당하는 가게가 없습니다."),
	NOT_FOUND_CATEGORY(404, "해당하는 카테고리가 없습니다."),
	NOT_FOUND_MENU(404, "해당하는 메뉴가 없습니다."),
	NOT_FOUND_STOREMENU(404, "해당하는 가게 메뉴가 없습니다."),
	NOT_FOUND_CART(404, "해당하는 장바구니가 없습니다."),
	NOT_FOUND_CARTMENU(404, "해당하는 장바구니 메뉴가 없습니다."),
	NOT_FOUND_ORDER(404, "해당하는 주문이 없습니다."),
	NOT_FOUND_LOCATION(404, "위치 정보를 입력하세요."),


	// 405 허용하지 않은 방법 Method Not Allowed
	NOT_SUPPORT_REQUEST_METHOD(404, "해당 request method를 지원하지 않습니다."),


	// 409 서버의 현재 상태와 요청이 충돌
	ALREADY_USERID_EXISTS(409, "이미 사용 중인 아이디입니다."),
	ALREADY_AUTH_EMAIL(409, "이미 이메일 인증을 완료한 계정입니다."),
	ALREADY_CATEGORY_NAME_EXISTS(409, "중복된 카테고리명입니다."),
	ALREADY_MENU_NAME_EXISTS(409, "중복된 메뉴명입니다."),
	ALREADY_STORE_NAME_EXISTS(409, "중복된 가게명입니다."),
	ORDER_ALREADY_CANCEL(400, "이미 취소된 주문입니다."),
	ALREADY_PAY_COMPLETE(400, "이미 결제 완료된 주문입니다."),


	// 415
	NOT_SUPPORT_MEDIA_TYPE(415, "해당 media type을 지원하지 않습니다."),


	// 500
	INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
	FAIL_SEND_MAIL(500, "메일 전송에 실패했습니다."),
	FAIL_CALL_KAKAO_API(500, "카카오 api 호출에 실패했습니다.")
	;

	private int code;
	private String message;

}
