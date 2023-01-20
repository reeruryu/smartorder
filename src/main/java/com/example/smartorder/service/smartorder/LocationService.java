package com.example.smartorder.service.smartorder;

import com.example.smartorder.dto.LocationDto;
import java.util.List;

public interface LocationService {

	/**
	 * 사용자(고객)의 위치를 저장합니다.
	 */
	void updateLocation(Double lat, Double lnt, String userId);

	/**
	 * 현재 위치 2km 반경 내 가까운 가게 10곳을 조회합니다.
	 */
	List<LocationDto> getNearStoreList(String userId);

}
