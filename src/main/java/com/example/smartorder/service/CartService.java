package com.example.smartorder.service;

import com.example.smartorder.model.AddCartMenu;

public interface CartService {


	/**
	 * 카트 메뉴를 추가합니다.
	 */
	void addCartMenu(AddCartMenu parameter, String userId);
}
