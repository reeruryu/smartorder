package com.example.smartorder.service.pay.impl;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.USER_NOT_EMAIL_AUTH;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_STOP;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_WITHDRAW;
import static com.example.smartorder.type.TransactionType.CHARGE;
import static com.example.smartorder.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.type.UserStatus.STATUS_STOP;
import static com.example.smartorder.type.UserStatus.STATUS_WITHDRAW;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.ConvPayDto;
import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.TransactionConvPay;
import com.example.smartorder.repository.ConvPayRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.TransactionConvPayRepository;
import com.example.smartorder.service.pay.ConvPayService;
import com.example.smartorder.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConvPayServiceImpl implements ConvPayService {

	private final ConvPayRepository convPayRepository;
	private final TransactionConvPayRepository transactionConvPayRepository;
	private final MemberRepository memberRepository;

	@Override
	public void addMoney(long amount, String userId) {

		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		validateUserStatus(member.getUserStatus());

		ConvPay convPay = convPayRepository.findByMember(member);
		if (convPay == null) { // 없으면 생성
			convPay = ConvPay.createConvPay(member, amount);
			convPayRepository.save(convPay);
		} else {
			convPay.addMoney(amount);
			convPayRepository.save(convPay);
		}

		transactionConvPayRepository.save(
			TransactionConvPay.builder()
				.convPay(convPay)
				.amount(amount)
				.transactionType(CHARGE)
				.build());
	}

	@Override
	public ConvPayDto getBalance(String userId) {
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		validateUserStatus(member.getUserStatus());

		ConvPay convPay = convPayRepository.findByMember(member);
		if (convPay == null) { // 없으면 생성
			convPay = ConvPay.createConvPay(member, 0);
			convPayRepository.save(convPay);
		}

		return ConvPayDto.of(convPay);
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
}
