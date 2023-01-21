package com.example.smartorder.service.pay;

import com.example.smartorder.dto.ConvPayDto;

public interface ConvPayService {

	/**
	 * 간편 페이에 amount원을 충전합니다.
	 */
	void addMoney(long amount, String userId);

	/**
	 * 사용자의 잔액을 조회합니다.
	 */
	ConvPayDto getBalance(String userId);
}
