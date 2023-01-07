package com.example.smartorder.service;

import com.example.smartorder.model.UpdateStoreOpenTime;
import java.time.LocalDateTime;

public interface StoreService {

	/**
	 * 가게 운영 요일을 변경합니다.
	 */
	public void updateStoreOpenDay(Long storeId, String openDayList);

	/**
	 * 가게의 open 시간을 변경합니다.
	 */
	public void updateStoreOpenTime(Long storeId, UpdateStoreOpenTime parameter);

	/**
	 * 가게 open 여부를 변경합니다.
	 */
	public void updateStoreOpenYn(Long storeId, boolean openYn);
}
