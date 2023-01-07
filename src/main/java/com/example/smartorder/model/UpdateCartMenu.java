package com.example.smartorder.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartMenu {
	@Min(value = 1, message = "최소 1개 이상 담아주세요.")
	int count;

	@NotNull
	Long cartMenuId;
}
