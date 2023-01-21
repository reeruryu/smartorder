package com.example.smartorder.service.pay;

import com.example.smartorder.model.PayParam;

public interface PayService {

	/**
	 * 결제
	 */
	void pay(PayParam parameter, String userId);
}
