package com.example.smartorder.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiHeader {
	private int resultCode;
	private String codeName;

}
