package com.example.smartorder.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class StoreParam {

	@Data
	public static class OpenDay {
		@NotNull(message = "요일을 선택해 주세요.")
		String openDayList;

	}

	@Data
	public static class OpenTime {
		@Min(0) @Max(23)
		int startHH;
		@Min(0) @Max(59)
		int startmm;

		@Min(0) @Max(23)
		int endHH;
		@Min(0) @Max(59)
		int endmm;

	}

	@Data
	public static class OpenYn {

		boolean openYn;

	}

}
