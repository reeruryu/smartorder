package com.example.smartorder.model;

import com.example.smartorder.type.PayType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PayParam {

	Long orderId;

	List<PayMethod> payMethodList;

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
	public static class PayMethod {
		long payPrice;
		PayType payType;

	}

}
