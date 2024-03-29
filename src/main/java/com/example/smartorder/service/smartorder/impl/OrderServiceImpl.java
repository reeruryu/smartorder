package com.example.smartorder.service.smartorder.impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CANCEL_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_CANCELED_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_COMPLETE_ORDER_STATE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_PREVIOUS_ORDER_STATE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_SAME_ORDER_STATE;
import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_ORDER;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.NOT_TODAY_ORDER;
import static com.example.smartorder.common.error.ErrorCode.ORDER_ALREADY_CANCEL;
import static com.example.smartorder.type.OrderState.BEFORE_COOKING;
import static com.example.smartorder.type.OrderState.COOKING;
import static com.example.smartorder.type.OrderState.PICKUP_COMPLETE;
import static com.example.smartorder.type.OrderState.PICKUP_REQ;
import static com.example.smartorder.type.PayState.BEFORE_PAY;
import static com.example.smartorder.type.PayState.PAY_CANCEL;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.OrderParam;
import com.example.smartorder.model.OrderParam.CeoUpdate;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.pay.PayService;
import com.example.smartorder.service.smartorder.OrderService;
import com.example.smartorder.type.OrderState;
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

	private final PayService payService;

	@Override
	public Long order(OrderDto orderDto) {

		Orders order = orderDto.toEntity();

		orderRepository.save(order);

		return order.getId();
	}

	@Override
	public Page<OrderHistDto> getOrderHist(Pageable pageable,
		LocalDate startDate, LocalDate endDate, String userId) {

		Member member = getMember(userId);

		validateDate(startDate, endDate);

		Page<Orders> orderHist = orderRepository.findByMemberAndRegDtBetween(member,
			startDate.atTime(0,0,0), endDate.atTime(23,59,59), pageable);

		return orderHist.map(OrderHistDto::of);
	}

	@Override
	public Long orderCancel(OrderParam.Cancel parameter, String userId) {

		Member member = getMember(userId);
		Orders order = getOrder(parameter.getOrderId());

		if (!member.equals(order.getMember())) {
			throw new CustomException(CANNOT_ACCESS_ORDER);
		}

		validateOrder(order);

		if (order.getPayState() != BEFORE_PAY) {
			payService.payCancel(order, member);
		}

		order.setOrderCancel(true);
		order.setOrderCancelReason(parameter.getOrderCancelReason());
		order.setCancelDt(LocalDateTime.now());
		orderRepository.save(order);

		return order.getId();
	}

	@Override
	public Page<OrderHistDto> getCeoOrderHist(Pageable pageable, LocalDate startDate,
		LocalDate endDate, Long storeId, String userId) {

		Member member = getMember(userId);
		Store store = getStore(storeId);

		// 본인 매장이 아닌 경우
		if (!member.equals(store.getMember())) {
			throw new CustomException(CANNOT_ACCESS_STORE);
		}

		validateDate(startDate, endDate);

		Page<Orders> orderHist = orderRepository.findByStoreAndRegDtBetween(store,
			startDate.atTime(0,0,0), endDate.atTime(23,59,59), pageable);

		return orderHist.map(OrderHistDto::of);
	}


	@Override
	public Long orderCeoCancel(OrderParam.CeoCancel parameter, String userId) {

		Member member = getMember(userId);
		Store store = getStore(parameter.getStoreId());
		Orders order = getOrder(parameter.getOrderId());

		if (!member.equals(store.getMember())) {
			throw new CustomException(CANNOT_ACCESS_STORE);
		}

		if (!store.equals(order.getStore())) {
			throw new CustomException(CANNOT_ACCESS_ORDER);
		}

		validateOrder(order);

		if (order.getPayState() != BEFORE_PAY) {
			payService.payCeoCancel(order);
		}

		order.setPayState(PAY_CANCEL);
		order.setOrderCancel(true);
		order.setOrderCancelReason(parameter.getOrderCancelReason());
		order.setCancelDt(LocalDateTime.now());
		orderRepository.save(order);

		return order.getId();
	}

	@Override
	public Long updateOrderState(CeoUpdate parameter, String userId) {

		Member member = getMember(userId);
		Store store = getStore(parameter.getStoreId());
		Orders order = getOrder(parameter.getOrderId());

		if (!member.equals(store.getMember())) {
			throw new CustomException(CANNOT_ACCESS_STORE);
		}

		if (order.isOrderCancel()) {
			throw new CustomException(CANNOT_CHANGE_CANCELED_ORDER);
		}

		OrderState preOrderState = order.getOrderState(); // 전 주문 상태
		OrderState orderState = parameter.getOrderState(); // 바꿀 주문 상태
		validateOrderState(preOrderState, orderState, order.getRegDt());

		order.setOrderState(orderState);
		orderRepository.save(order);

		return order.getId();
	}


	private Member getMember(String userId) {
		return memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	private Store getStore(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));
	}

	private Orders getOrder(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_ORDER));
	}

	private void validateDate(LocalDate startDate, LocalDate endDate) {
		if (startDate.isAfter(endDate)) {
			throw new CustomException(END_FASTER_THAN_START);
		}
	}

	private void validateOrder(Orders order) {

		if (order.isOrderCancel()) {
			throw new CustomException(ORDER_ALREADY_CANCEL);
		}

		if (BEFORE_COOKING != order.getOrderState()) {
			throw new CustomException(CANNOT_CANCEL_ORDER);
		}
	}

	private void validateOrderState(OrderState preOrderState, OrderState orderState,
		LocalDateTime dateTime) {

		if (!dateTime.toLocalDate().equals(LocalDate.now())) {
			throw new CustomException(NOT_TODAY_ORDER);
		}

		if (preOrderState == orderState) {
			throw new CustomException(CANNOT_CHANGE_SAME_ORDER_STATE);
		}

		if (preOrderState == PICKUP_COMPLETE) {
			throw new CustomException(CANNOT_CHANGE_COMPLETE_ORDER_STATE);
		}

		if (preOrderState == COOKING && orderState == BEFORE_COOKING) {
			throw new CustomException(CANNOT_CHANGE_PREVIOUS_ORDER_STATE);
		}

		if (preOrderState == PICKUP_REQ &&
			(orderState == BEFORE_COOKING || orderState == COOKING)) {
			throw new CustomException(CANNOT_CHANGE_PREVIOUS_ORDER_STATE);
		}
	}
}
