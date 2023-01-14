package com.example.smartorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderMenuDto {

	String menuName;
	long menuPrice;
	int menuCount;

}
