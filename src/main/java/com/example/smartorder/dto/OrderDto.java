package com.example.smartorder.dto;

import com.example.smartorder.entity.Store;
import com.example.smartorder.member.entity.Member;
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
public class OrderDto {
	Member member;
	Store store;
	List<Map<String, Object>> orderMenu;
	long orderPrice;
}