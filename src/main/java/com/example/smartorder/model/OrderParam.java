package com.example.smartorder.model;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderParam {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Cancel {
		@NotNull(message = "주문 id를 입력해 주세요.")
		Long orderId;

		@NotNull(message = "주문 취소 사유를 입력해 주세요.")
		String orderCancelReason;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CeoCancel {
		@NotNull(message = "주문 id를 입력해 주세요.")
		Long orderId;

		@NotNull(message = "가게 id를 입력해 주세요.")
		Long storeId;

		@NotNull(message = "주문 취소 사유를 입력해 주세요.")
		String orderCancelReason;
	}

}
