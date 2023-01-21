package com.example.smartorder.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayType {
	CONV_PAY("간편 페이"),
	POINT("포인트")
	;

	private final String description;

}
