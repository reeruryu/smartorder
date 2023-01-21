package com.example.smartorder.service.pay;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.model.PayParam;

public interface PayService {

	/**
	 * 결제
	 */
	void pay(PayParam parameter, String userId);

	/**
	 * 결제 취소
	 */
	void payCancel(Orders order, Member member);

	/**
	 * 점주의 주문 취소로 인한 결제 취소
	 */
	void payCeoCancel(Orders order);
}
