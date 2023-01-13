package com.example.smartorder.service;

import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.model.UpdateStoreMenu;
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

	/**
	 * 가게의 메뉴를 수정합니다.
	 */
	boolean updateStoreMenu(Long storeId, Long storeMenuId, UpdateStoreMenu parameter);

	/**
	 * 하루만 품절로 설정된 가게메뉴를
	 * 모두 오전 12시 마다 판매 중으로 변경합니다.
	 */
	void updateSaleState();
}
