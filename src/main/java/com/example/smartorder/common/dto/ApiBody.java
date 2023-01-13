package com.example.smartorder.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiBody<T> {
	private T data;
	private T msg;

}
