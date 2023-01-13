package com.example.smartorder.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SaleState {
	SOLDOUT_FOR_ONE_DAY("하루 품절"), // ceo의 메뉴 관리를 위해 추가
	SOLDOUT("품절"),
	ON_SALE("판매 중")
	;

	private final String description;


}
