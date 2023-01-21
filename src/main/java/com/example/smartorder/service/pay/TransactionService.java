package com.example.smartorder.service.pay;

import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Point;

public interface TransactionService {

	/**
	 * 간편 페이를 사용해 결제합니다.
	 */
	void useConvPay(ConvPay convPay, long amount);

	/**
	 * 포인트를 사용해 결제합니다.
	 */
	void usePoint(Point convPay, long amount, long earnPoint);

	/**
	 * 간편 페이 결제를 취소합니다.
	 */
	void cancelConvPay(Member member, long amount);

	/**
	 * 포인트 결제를 취소합니다.
	 */
	void cancelPoint(Member member, long amount, long earnPoint);
}
