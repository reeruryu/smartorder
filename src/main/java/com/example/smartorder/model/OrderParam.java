package com.example.smartorder.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

public class OrderParam {

	@Data
	public static class Cancel {
		@NotNull(message = "주문 id를 입력해 주세요.")
		Long orderId;

		@NotNull(message = "주문 취소 사유를 입력해 주세요.")
		String orderCancelReason;
	}

	@Data
	public static class CeoCancel {
		@NotNull@NotNull(message = "주문 id를 입력해 주세요.")
		Long orderId;

		@NotNull@NotNull(message = "가게 id를 입력해 주세요.")
		Long storeId;

		@NotNull@NotNull(message = "주문 취소 사유를 입력해 주세요.")
		String orderCancelReason;
	}

}
