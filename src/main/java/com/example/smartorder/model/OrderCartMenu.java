package com.example.smartorder.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCartMenu {
	@NotNull
	Long cartId;
}
