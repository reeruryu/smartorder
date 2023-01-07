package com.example.smartorder.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayState {

	BEFORE_ORDER_RECEIVE("주문 접수 전"),
	COOKING("조리 중"),
	PICKUP_REQ("픽업 요청"),

	COMPLETE_COOK("조리 완료")
	;

	private final String description;
}
