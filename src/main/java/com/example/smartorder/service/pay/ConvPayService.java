package com.example.smartorder.service.pay;

public interface ConvPayService {

	/**
	 * 간편 페이에 amount원을 충전합니다.
	 */
	void addMoney(long amount, String userId);
}
