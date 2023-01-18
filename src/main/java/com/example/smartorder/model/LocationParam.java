package com.example.smartorder.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

public class LocationParam {

	@Data
	public static class Customer {
		@NotNull(message = "위도 정보가 필요합니다.")
		Double lat;

		@NotNull(message = "경도 정보가 필요합니다.")
		Double lnt;
	}

}
