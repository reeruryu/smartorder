package com.example.smartorder.service.Impl;

import static com.example.smartorder.type.PayState.*;

import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

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
}
