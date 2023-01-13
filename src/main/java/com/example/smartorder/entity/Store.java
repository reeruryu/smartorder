package com.example.smartorder.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Slf4j
public class Store extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Member member;

	private String storeName;

	private String zipcode;

	private String addr;
	private String addrDetail;

	private double lat;
	private double lnt;

	private LocalTime startTime;
	private LocalTime endTime;

	private String openDay; // ex) 0,2,3,6 (0 = Monday, 1 = Tuesday,..... 6 = Sunday)

	private boolean openYn;

	public boolean isOpenDay(LocalDateTime now) {
		int todayDay = LocalDateTime.now().getDayOfWeek().getValue();
		log.info("오늘은 " + String.valueOf(todayDay) + "요일");

		return this.openDay.contains(String.valueOf(todayDay));
	}

	public boolean isOpenTime(LocalTime now) {
		if (this.startTime.isAfter(now)) {
			log.info("startTime 검사");
			return false;
		}

		if (this.endTime.isBefore(now)) {
			log.info("endTime 검사");
			return false;
		}

		return true;
	}

	public List<Integer> getDayList() {
		List<Integer> dayList = new ArrayList<>();
		for (String s: this.openDay.split(",")) {
			dayList.add(Integer.valueOf(s));
		}

		return dayList;
	}

}
