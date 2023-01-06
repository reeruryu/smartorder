package com.example.smartorder.model;

import com.example.smartorder.type.SaleState;
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
public class UpdateStoreMenu {

	@NotNull
	SaleState saleState;

	@NotNull
	boolean hiddenYn;

}
