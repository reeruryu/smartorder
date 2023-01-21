package com.example.smartorder.model;

import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ConvPayParam {

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class AddMoney {
		@Min(value = 5000, message = "5,000원부터 충전이 가능합니다.")
		long amount;
	}



}
