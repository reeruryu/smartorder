package com.example.smartorder.service;

import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Menu;
import java.util.List;

public interface StoreMenuService {

	/**
	 * 모든 메뉴를 가게에 생성합니다. (가게 첫 등록 시)
	 */
	boolean createAllStoreMenu(Long storeId);

	/**
	 * 신 메뉴를 모든 가게에 생성합니다.
	 */
	boolean createNewStoreMenu(List<Long> menuList);

	/**
	 * 가게의 모든 메뉴를 보여줍니다.
	 */
	List<StoreMenuDto> listStoreMenu(Long storeId, Long categoryId);
}
