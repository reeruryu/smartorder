package com.example.smartorder.dto;

import com.example.smartorder.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PointDto {
	Long id;

	String userId;

	long balance;

	public static PointDto of(Point point) {
		return PointDto.builder()
			.id(point.getId())
			.userId(point.getMember().getUserId())
			.balance(point.getBalance())
			.build();
	}

}
