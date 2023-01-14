package com.example.smartorder.service.smartorder;

import com.example.smartorder.dto.FrontStoreMenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.model.StoreMenuParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreMenuService {

	/**
	 * 가게의 모든 메뉴를 보여줍니다.
	 */
	List<StoreMenuDto> listStoreMenu(Long storeId, Long categoryId, String userId);

	/**
	 * 가게의 메뉴를 수정합니다.
	 */
	void updateStoreMenu(Long storeId, Long storeMenuId, StoreMenuParam.Update parameter, String userId);

	/**
	 * 하루만 품절로 설정된 가게메뉴를
	 * 모두 오전 12시 마다 판매 중으로 변경합니다.
	 */
	void updateSaleState();

	/**
	 * 고객(front)에 가게 메뉴를 보여줍니다.
	 * (숨김 메뉴는 보이지 않습니다.)
	 */
	Page<FrontStoreMenuDto> frontStoreMenu(Long storeId, Long categoryId, Pageable pageable, String userId);
}
