package com.example.smartorder.model;

import com.example.smartorder.type.PayType;
import com.example.smartorder.type.SaleState;
import com.example.smartorder.type.valid.EnumTypeValid;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PayParam {

	@NotNull(message = "주문 번호를 입력해 주세요.")
	Long orderId;

	@NotNull(message = "결제 방식이 입력되지 않았습니다.")
	List<PayMethod> payMethodList;

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class PayMethod {

		@Min(value = 0, message = "최소 0원 이상 입력해 주세요.")
		long payPrice;

		@EnumTypeValid(enumClass = SaleState.class, message = "invalid PayType")
		@NotNull(message = "PayType를 선택하세요.")
		PayType payType;

	}

}
