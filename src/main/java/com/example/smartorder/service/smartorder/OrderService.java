package com.example.smartorder.service.smartorder;

import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.model.OrderParam;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {


	/*
	 * 주문을 저장합니다.
	 */
	Long order(OrderDto orderDto);

	/**
	 * 고객의 주어진 기간의 주문 내역을 조회합니다.
	 */
	Page<OrderHistDto> getOrderHist(Pageable pageable,
		LocalDate startDate, LocalDate endTime, String userId);

	/**
	 * 고객이 주문을 취소합니다.
	 */
	Long orderCancel(OrderParam.Cancel parameter, String userId);

	/**
	 * 점주의 주어진 기간의 주문 내역을 조회합니다.
	 */
	Page<OrderHistDto> getCeoOrderHist(Pageable pageable,
		LocalDate startDate, LocalDate endDate, Long storeId, String userId);

	/**
	 * 점주가 주문을 취소합니다.
	 */
	Long orderCeoCancel(OrderParam.CeoCancel parameter, String userId);
}
