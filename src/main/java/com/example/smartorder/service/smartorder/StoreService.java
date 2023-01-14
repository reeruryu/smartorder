package com.example.smartorder.service.smartorder;

import com.example.smartorder.model.StoreParam;

public interface StoreService {

	/**
	 * 가게 운영 요일을 변경합니다.
	 */
	public void updateStoreOpenDay(Long storeId, String openDayList, String userId);

	/**
	 * 가게의 open 시간을 변경합니다.
	 */
	public void updateStoreOpenTime(Long storeId, StoreParam.OpenTime parameter, String userId);

	/**
	 * 가게 open 여부를 변경합니다.
	 */
	public void updateStoreOpenYn(Long storeId, boolean openYn, String userId);
}
