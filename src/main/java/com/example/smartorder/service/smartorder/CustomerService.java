package com.example.smartorder.service.smartorder;

import com.example.smartorder.dto.StoreDto;
import java.util.List;

public interface CustomerService {

	/**
	 * 가까운 가게 최대 10개를 보여줍니다.
	 */
	List<StoreDto> getNearStoreList(String userId, double lat, double lnt);
}
