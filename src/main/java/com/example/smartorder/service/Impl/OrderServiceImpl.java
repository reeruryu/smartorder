package com.example.smartorder.service.Impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CANCEL_ORDER;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_ORDER;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.ORDER_ALREADY_CANCEL;
import static com.example.smartorder.type.OrderState.*;
import static com.example.smartorder.type.PayState.*;

import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.model.OrderCancel;
import com.example.smartorder.model.OrderCeoCancel;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.OrderService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	@Override
	public Long orderCancel(OrderCancel parameter, String userId) {

		Member member = getMember(userId);
		Orders order = getOrder(parameter.getOrderId());

		if (!member.equals(order.getMember())) {
			throw new NotFoundException(CANNOT_ACCESS_ORDER);
		}

		if (order.isOrderCancel()) {
			throw new NotFoundException(ORDER_ALREADY_CANCEL);
		}

		if (BEFORE_COOKING != order.getOrderState()) {
			throw new NotFoundException(CANNOT_CANCEL_ORDER);
		}

		// 결제 전이면 끝, 결제완료면 취소 진행

		order.setOrderCancel(true);
		order.setOrderCancelReason(parameter.getOrderCancelReason());
		order.setCancelDt(LocalDateTime.now());
		orderRepository.save(order);

		return order.getId();
	}

	@Override
	public Long orderCeoCancel(OrderCeoCancel parameter, String userId) {

		Member member = getMember(userId);
		Store store = getStore(parameter.getStoreId());
		Orders order = getOrder(parameter.getOrderId());

		if (!member.equals(store.getMember())) {
			throw new NotFoundException(CANNOT_ACCESS_STORE);
		}

		if (!store.equals(order.getStore())) {
			throw new NotFoundException(CANNOT_ACCESS_ORDER);
		}

		if (order.isOrderCancel()) {
			throw new NotFoundException(ORDER_ALREADY_CANCEL);
		}

		// order.getMember()
		//  -> 고객 결제 취소 진행

		order.setPayState(PAY_CANCEL);
		order.setOrderCancel(true);
		order.setOrderCancelReason(parameter.getOrderCancelReason());
		order.setCancelDt(LocalDateTime.now());
		orderRepository.save(order);

		return order.getId();
	}

	private Member getMember(String userId) {
		return memberRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
	}

	private Store getStore(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
	}

	private Orders getOrder(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
	}
}
