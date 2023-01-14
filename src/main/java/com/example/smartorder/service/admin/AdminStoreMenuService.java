package com.example.smartorder.service.admin;

import java.util.List;

public interface AdminStoreMenuService {

	/**
	 * 모든 메뉴를 가게에 생성합니다. (가게 첫 등록 시)
	 */
	void addAllStoreMenu(Long storeId);

	/**
	 * 신 메뉴를 모든 가게에 생성합니다.
	 */
	void addNewStoreMenu(List<Long> menuList);

}
