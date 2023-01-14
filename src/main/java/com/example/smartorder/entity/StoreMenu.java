package com.example.smartorder.entity;

import com.example.smartorder.type.SaleState;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class StoreMenu extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Menu menu;

	@ManyToOne
	private Store store;

	@Enumerated(EnumType.STRING)
	private SaleState saleState;

	private LocalDate soldoutDt; // 스케쥴러로 구현해서 삭제할 예정
	private boolean hiddenYn;

}
