package com.example.smartorder.entity;

import com.example.smartorder.member.entity.BaseEntity;
import com.example.smartorder.type.SaleState;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class StoreMenu extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Menu menu;

	@ManyToOne
	private Store store;

	@Enumerated(EnumType.STRING)
	private SaleState saleState;

	private String soldoutDt;
	private boolean hiddenYn;

}
