package com.example.smartorder.service.pay;

import com.example.smartorder.dto.TransactionConvPayDto;
import com.example.smartorder.dto.TransactionPointDto;
import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	/**
	 * 간편페이 거래 내역을 조회합니다.
	 */
	Page<TransactionConvPayDto> getConvPayTransaction(Pageable pageable, String userId);

	/**
	 * 포인트 거래 내역을 조회합니다.
	 */
	Page<TransactionPointDto> getPointTransaction(Pageable pageable, String userId);

}
