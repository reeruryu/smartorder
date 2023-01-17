package com.example.smartorder.service.smartorder;

import com.example.smartorder.dto.FrontStoreMenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.type.SaleState;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreMenuService {

	/**
	 * 가게의 모든 메뉴를 보여줍니다.
	 */
	List<StoreMenuDto> listStoreMenu(Long storeId, Long categoryId, String userId);

	/**
	 * 가게의 메뉴 숨김 여부를 수정(설정)합니다.
	 */
	void updateHiddenYn(Long storeId, Long storeMenuId, boolean hiddenYn, String userId);

	/**
	 * 가게의 메뉴 판매 여부를 수정(설정)합니다.
	 */
	void updateSaleState(Long storeId, Long storeMenuId, SaleState saleState, String userId);

	/**
	 * 고객(front)에 가게 메뉴를 보여줍니다.
	 * (숨김 메뉴는 보이지 않습니다.)
	 */
	Page<FrontStoreMenuDto> frontStoreMenu(Long storeId, Long categoryId, Pageable pageable, String userId);
}
