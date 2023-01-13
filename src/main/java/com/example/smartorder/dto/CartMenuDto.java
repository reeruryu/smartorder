package com.example.smartorder.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartMenuDto {
	Long id;
	private int menuCount;


	String menuName;
	long menuPrice;

	Long storeId;

}
