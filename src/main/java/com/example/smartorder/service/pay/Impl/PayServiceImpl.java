package com.example.smartorder.service.pay.Impl;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_PAY_COMPLETE;
import static com.example.smartorder.common.error.ErrorCode.INVALID_PAYMENT_REQUEST;
import static com.example.smartorder.common.error.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_ORDER;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.USER_NOT_EMAIL_AUTH;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_STOP;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_WITHDRAW;
import static com.example.smartorder.type.PayState.PAY_COMPLETE;
import static com.example.smartorder.type.PayType.CONV_PAY;
import static com.example.smartorder.type.PayType.POINT;
import static com.example.smartorder.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.type.UserStatus.STATUS_STOP;
import static com.example.smartorder.type.UserStatus.STATUS_WITHDRAW;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Pay;
import com.example.smartorder.entity.Point;
import com.example.smartorder.model.PayParam;
import com.example.smartorder.repository.ConvPayRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.repository.PayRepository;
import com.example.smartorder.repository.PointRepository;
import com.example.smartorder.service.pay.PayService;
import com.example.smartorder.service.pay.TransactionService;
import com.example.smartorder.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {

	private final MemberRepository memberRepository;
	private final OrderRepository orderRepository;

	private final ConvPayRepository convPayRepository;
	private final PointRepository pointRepository;
	private final PayRepository payRepository;

	private final TransactionService transactionService;


	@Override
	public void pay(PayParam parameter, String userId) {
		// 유저 체크
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		validateUserStatus(member.getUserStatus());

		// 주문 체크
		Orders order = orderRepository.findById(parameter.getOrderId())
				.orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
		if (order.getPayState() == PAY_COMPLETE) {
			throw new CustomException(ALREADY_PAY_COMPLETE);
		}

		// request 체크
		long convPayAmount = 0;
		long pointAmount = 0;
		for (PayParam.PayMethod payMethod: parameter.getPayMethodList()) {
			if (payMethod.getPayType() == CONV_PAY) {
				convPayAmount = payMethod.getPayPrice();
			} else if (payMethod.getPayType() == POINT) {
				pointAmount = payMethod.getPayPrice();
			}
		}
		if (order.getOrderPrice() > convPayAmount + pointAmount) {
			throw new CustomException(INVALID_PAYMENT_REQUEST);
		}

		// 잔액 체크
		ConvPay convPay = convPayRepository.findByMember(member);
		Point point = pointRepository.findByMember(member);
		validateBalance(convPayAmount, pointAmount, convPay, point);

		// 결제 내역 저장
		if (convPayAmount > 0) { // 간편 페이 사용
			transactionService.useConvPay(convPay, convPayAmount);
			payRepository.save(Pay.builder()
					.orderId(parameter.getOrderId())
					.payPrice(convPayAmount)
					.payType(CONV_PAY)
					.payState(PAY_COMPLETE)
					.build());
		}

		if (pointAmount > 0) { // 포인트 사용
			transactionService.usePoint(point, pointAmount, (long) (convPayAmount * 0.05));
			payRepository.save(Pay.builder()
				.orderId(parameter.getOrderId())
				.payPrice(pointAmount)
				.payType(POINT)
				.payState(PAY_COMPLETE)
				.build());
		}

	}

	private void validateUserStatus(UserStatus userStatus) {
		if (userStatus == STATUS_EMAIL_REQ) {
			throw new CustomException(USER_NOT_EMAIL_AUTH);
		}

		if (userStatus == STATUS_STOP) {
			throw new CustomException(USER_STATUS_STOP);
		}

		if (userStatus == STATUS_WITHDRAW) {
			throw new CustomException(USER_STATUS_WITHDRAW);
		}
	}
	private void validateBalance(long convPayAmount, long pointAmount,
		ConvPay convPay, Point point) {

		if (convPay == null && convPayAmount > 0) {
			throw new CustomException(NOT_ENOUGH_BALANCE);
		}

		if (point == null && pointAmount > 0) {
			throw new CustomException(NOT_ENOUGH_BALANCE);
		}

		if (convPay.getBalance() < convPayAmount ||
			point.getBalance() < pointAmount) {
			throw new CustomException(NOT_ENOUGH_BALANCE);
		}
	}

}
