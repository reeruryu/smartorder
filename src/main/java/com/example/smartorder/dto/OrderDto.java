package com.example.smartorder.dto;

import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.Member;
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
}
