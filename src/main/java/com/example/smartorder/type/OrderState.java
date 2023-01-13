package com.example.smartorder.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderState {
	BEFORE_COOKING("조리 전"),
	COOKING("조리 중"),
	PICKUP_REQ("픽업 요청"),

	PICKUP_COMPLETE("픽업 완료")
	;

	private final String description;
}
