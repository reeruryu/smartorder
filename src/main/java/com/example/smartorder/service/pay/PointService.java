package com.example.smartorder.service.pay;

import com.example.smartorder.dto.PointDto;

public interface PointService {

	/**
	 * 사용자의 포인트를 조회합니다.
	 */
	PointDto getBalance(String userId);
}
