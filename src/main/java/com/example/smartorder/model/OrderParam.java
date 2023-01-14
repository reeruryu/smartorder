package com.example.smartorder.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

public class OrderParam {

	@Data
	public static class Cancel {
		@NotNull
		Long orderId;

		String orderCancelReason;
	}

	@Data
	public static class CeoCancel {
		@NotNull
		Long orderId;

		@NotNull
		Long storeId;

		@NotNull
		String orderCancelReason;
	}

}
