package com.example.smartorder.entity;

import com.example.smartorder.type.SaleState;
import java.time.LocalDateTime;
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

	private LocalDateTime soldOutDt; // 하루만 품절 설정 날짜
	private boolean hiddenYn;

}
