package com.example.smartorder.service;

import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.model.AddCartMenu;
import com.example.smartorder.model.UpdateCartMenu;
import java.util.List;

public interface OrderService {


	/*
	 * 주문을 저장합니다.
	 */
	Long order(OrderDto orderDto);
}
