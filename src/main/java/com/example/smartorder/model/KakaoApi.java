package com.example.smartorder.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class KakaoApi {

	private List<Documents> documents;

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class Documents {
		private String x;
		private String y;
	}
}
