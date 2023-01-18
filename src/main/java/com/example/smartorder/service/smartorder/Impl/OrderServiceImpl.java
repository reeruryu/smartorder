package com.example.smartorder.service.smartorder.Impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CANCEL_ORDER;
import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_ORDER;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.ORDER_ALREADY_CANCEL;
import static com.example.smartorder.type.OrderState.BEFORE_COOKING;
import static com.example.smartorder.type.PayState.BEFORE_PAY;
import static com.example.smartorder.type.PayState.PAY_CANCEL;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.OrderParam;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.repository.StoreRepository;
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

		// TODO 결제 전이면 끝, 결제완료면 취소 진행

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

		// TODO 고객 결제 취소 진행

		order.setPayState(PAY_CANCEL);
		order.setOrderCancel(true);
		order.setOrderCancelReason(parameter.getOrderCancelReason());
		order.setCancelDt(LocalDateTime.now());
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
}
