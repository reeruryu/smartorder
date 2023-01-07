package com.example.smartorder.entity;

import com.example.smartorder.member.entity.BaseEntity;
import com.example.smartorder.member.entity.Member;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
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

	private long lat;
	private long lnt;

	private LocalTime startTime;
	private LocalTime endTime;

	private String openDay; // ex) 0,2,3,6 (0 = Monday, 1 = Tuesday,..... 6 = Sunday)

	private boolean openYn;

}
