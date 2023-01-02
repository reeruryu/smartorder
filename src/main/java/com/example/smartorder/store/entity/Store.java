package com.example.smartorder.store.entity;

import com.example.smartorder.member.entity.BaseEntity;
import com.example.smartorder.member.entity.Member;
import java.time.LocalDateTime;
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

	//	private LocalDateTime startTime;
//	private LocalDateTime endTime;
//	private LocalDateTime openDay;
	private boolean openYn;

}
