package com.example.smartorder.service.pay.Impl;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.USER_NOT_EMAIL_AUTH;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_STOP;
import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_WITHDRAW;
import static com.example.smartorder.type.UserStatus.STATUS_EMAIL_REQ;
import static com.example.smartorder.type.UserStatus.STATUS_STOP;
import static com.example.smartorder.type.UserStatus.STATUS_WITHDRAW;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.PointDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Point;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.PointRepository;
import com.example.smartorder.service.pay.PointService;
import com.example.smartorder.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final PointRepository pointRepository;
	private final MemberRepository memberRepository;

	@Override
	public PointDto getBalance(String userId) {
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		validateUserStatus(member.getUserStatus());

		Point point = pointRepository.findByMember(member);
		if (point == null) { // 없으면 생성
			point = Point.createPoint(member, 0);
			pointRepository.save(point);
		}

		return PointDto.of(point);
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
