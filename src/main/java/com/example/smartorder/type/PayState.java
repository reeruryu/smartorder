package com.example.smartorder.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayState {

	BEFORE_PAY("결제 전"),
	PAY_COMPLETE("결제 완료"),
	PAY_CANCEL("결제 취소")
	;

	private final String description;
}
