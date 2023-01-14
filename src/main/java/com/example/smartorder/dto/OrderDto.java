package com.example.smartorder.dto;

import static com.example.smartorder.type.PayState.BEFORE_PAY;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import java.util.List;
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
public class OrderDto {
	Member member;
	Store store;
	List<OrderMenuDto> orderMenu;
	long orderPrice;

	public Orders toEntity() {
		return Orders.builder()
			.member(this.member)
			.store(this.store)
			.orderMenu(this.orderMenu)
			.orderPrice(this.orderPrice)
			.payState(BEFORE_PAY)
			.build();
	}
}
