package com.example.smartorder.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CartParam {

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class Add {
		@NotNull(message = "가게 메뉴를 선택하세요.")
		Long storeMenuId;

		@Min(value = 1, message = "최소 1개 이상 담아주세요.")
		int menuCount;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Update {
		@Min(value = 1, message = "최소 1개 이상 담아주세요.")
		int count;

		@NotNull(message = "장바구니에 담긴 메뉴를 선택하세요.")
		Long cartMenuId;
	}

	@Data
	public static class Order {
		@NotNull(message = "카트id를 입력해 주세요")
		Long cartId;
	}

}
