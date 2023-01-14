package com.example.smartorder.entity;

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
public class CartMenu extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cart cart;

	@ManyToOne
	private StoreMenu storeMenu;

	private int menuCount;

	public static CartMenu createCartMenu(Cart cart, StoreMenu storeMenu, int count) {

		return CartMenu.builder()
			.cart(cart)
			.storeMenu(storeMenu)
			.menuCount(count)
			.build();
	}

	public void addCount(int count) {
		this.menuCount += count;
	}

}
