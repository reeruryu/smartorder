package com.example.smartorder.dto;

import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.type.OrderState;
import com.example.smartorder.type.PayState;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistDto {
	Long orderId;
	Long storeId;
	List<OrderMenuDto> orderMenu;
	LocalDateTime regDt;

	long orderPrice;
	PayState payState;
	OrderState orderState;
	String orderCancelReason;


	public static OrderHistDto of(Orders orders) {
		return OrderHistDto.builder()
			.orderId(orders.getId())
			.storeId(orders.getStore().getId())
			.orderMenu(orders.getOrderMenu())
			.orderPrice(orders.getOrderPrice())
			.regDt(orders.getRegDt())
			.payState(orders.getPayState())
			.orderState(orders.getOrderState())
			.orderCancelReason(orders.getOrderCancelReason())
			.build();
	}

}
