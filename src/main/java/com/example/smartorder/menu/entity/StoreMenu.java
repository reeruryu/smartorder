package com.example.smartorder.menu.entity;

import com.example.smartorder.member.entity.BaseEntity;
import com.example.smartorder.store.entity.Store;
import javax.persistence.Entity;
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

	@ManyToOne
	private Menu menu;

	@OneToOne
	private Store store;

	private String saleState;

	private String soldoutDt;
	private boolean hiddenYn;

}
