package com.example.smartorder.model;

import com.example.smartorder.type.valid.EnumTypeValid;
import com.example.smartorder.type.SaleState;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class StoreMenuParam {

	@Data
	public static class UpdateHidden {
		boolean hidden;
	}

	@Data
	public static class UpdateSaleState {
		@EnumTypeValid(enumClass = SaleState.class, message = "invalid SaleState")
		@NotNull(message = "SaleState를 선택하세요.")
		SaleState saleState;
	}

}
