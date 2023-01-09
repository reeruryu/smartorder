package com.example.smartorder.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCancel {

	@NotNull
	Long orderId;

	String orderCancelReason;

}
