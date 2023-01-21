package com.example.smartorder.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
	CHARGE("충전"),
	EARN("포인트 획득"),
	EARN_CANCEL("포인트 획득 취소"),
	USE("사용"),
	CANCEL("취소")
	;

	private final String description;
}
