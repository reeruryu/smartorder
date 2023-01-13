package com.example.smartorder.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddCartMenu {

	@NotNull
	Long storeMenuId;

	@Min(value = 1, message = "최소 1개 이상 담아주세요.")
	int menuCount;

}
