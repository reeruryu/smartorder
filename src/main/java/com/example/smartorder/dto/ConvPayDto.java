package com.example.smartorder.dto;

import com.example.smartorder.entity.ConvPay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ConvPayDto {
	Long id;

	String userId;

	long balance;

	public static ConvPayDto of(ConvPay convPay) {
		return ConvPayDto.builder()
			.id(convPay.getId())
			.userId(convPay.getMember().getUserId())
			.balance(convPay.getBalance())
			.build();
	}

}
