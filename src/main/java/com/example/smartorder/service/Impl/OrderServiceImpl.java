package com.example.smartorder.service.Impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.OrderState.*;
import static com.example.smartorder.type.PayState.*;

import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.OrderService;
import com.example.smartorder.type.OrderState;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final StoreRepository storeRepository;

	@Override
	public Long order(OrderDto orderDto) {

		Orders order = Orders.builder()
			.member(orderDto.getMember())
			.store(orderDto.getStore())
			.orderMenu(orderDto.getOrderMenu())
			.orderPrice(orderDto.getOrderPrice())
			.payState(BEFORE_PAY)
			.build();
		orderRepository.save(order);

		return order.getId();
	}

	@Override
	public Page<OrderHistDto> getOrderHist(Pageable pageable,
		LocalDate startDate, LocalDate endDate, String userId) {

		Member member = getMember(userId);

		Page<Orders> orderHist = orderRepository.findByMemberAndRegDtBetween(member,
			startDate.atTime(0,0,0), endDate.atTime(23,59,59), pageable);

		return orderHist.map(OrderHistDto::of);
	}

	@Override
	public Page<OrderHistDto> getCeoOrderHist(Pageable pageable, LocalDate startDate,
		LocalDate endDate, Long storeId, String userId) {
		Member member = getMember(userId);
		Store store = getStore(storeId);

		// 본인 매장이 아닌 경우
		if (!member.equals(store.getMember())) {
			throw new NotFoundException(CANNOT_ACCESS_STORE);
		}

		Page<Orders> orderHist = orderRepository.findByStoreAndRegDtBetween(store,
			startDate.atTime(0,0,0), endDate.atTime(23,59,59), pageable);

		return orderHist.map(OrderHistDto::of);
	}

	private Member getMember(String userId) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
		return member;
	}

	private Store getStore(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
		return store;
	}
}
