package com.example.smartorder.service.pay.Impl;

import static com.example.smartorder.type.TransactionType.*;

import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Point;
import com.example.smartorder.entity.TransactionConvPay;
import com.example.smartorder.entity.TransactionPoint;
import com.example.smartorder.repository.ConvPayRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.PointRepository;
import com.example.smartorder.repository.TransactionConvPayRepository;
import com.example.smartorder.repository.TransactionPointRepository;
import com.example.smartorder.service.pay.TransactionService;
import com.example.smartorder.type.TransactionType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

	private final TransactionConvPayRepository transactionConvPayRepository;
	private final TransactionPointRepository transactionPointRepository;
	private final ConvPayRepository convPayRepository;
	private final PointRepository pointRepository;
	private final MemberRepository memberRepository;

	@Override
	public void useConvPay(ConvPay convPay, long amount) {

		// ConvPay set & save
		convPay.minusMoney(amount);

		convPayRepository.save(convPay);

		// TransactionConvPay save
		transactionConvPayRepository.save(TransactionConvPay.builder()
			.convPay(convPay)
			.amount(amount)
			.transactionType(USE)
			.build());
	}

	@Override
	public void usePoint(Point point, long amount, long earnPoint) {

		// point set & save
		point.minusMoney(amount);
		if (earnPoint > 0) {
			point.addMoney(earnPoint);
		}
		pointRepository.save(point);

		// transactionPoint save
		transactionPointRepository.save(TransactionPoint.builder()
			.point(point)
			.amount(amount)
			.transactionType(USE)
			.build());

		if (earnPoint > 0) {
			transactionPointRepository.save(TransactionPoint.builder()
				.point(point)
				.amount(earnPoint)
				.transactionType(EARN)
				.build());
		}

	}

	@Override
	public void cancelConvPay(Member member, long amount) {

		// ConvPay set & save
		ConvPay convPay = convPayRepository.findByMember(member);
		convPay.addMoney(amount);
		convPayRepository.save(convPay);

		// TransactionConvPay save
		transactionConvPayRepository.save(TransactionConvPay.builder()
			.convPay(convPay)
			.amount(amount)
			.transactionType(CANCEL)
			.build());
	}

	@Override
	public void cancelPoint(Member member, long amount, long earnPoint) {

		// point set & save
		Point point = pointRepository.findByMember(member);
		point.addMoney(amount);
		if (earnPoint > 0) {
			point.minusMoney(earnPoint);
		}
		pointRepository.save(point);

		// transactionPoint save
		transactionPointRepository.save(TransactionPoint.builder()
			.point(point)
			.amount(amount)
			.transactionType(CANCEL)
			.build());

		if (earnPoint > 0) {
			transactionPointRepository.save(TransactionPoint.builder()
				.point(point)
				.amount(earnPoint)
				.transactionType(EARN_CANCEL)
				.build());
		}
	}
}
