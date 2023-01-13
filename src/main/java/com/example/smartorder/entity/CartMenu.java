package com.example.smartorder.entity;

import com.example.smartorder.member.entity.Member;
import java.time.LocalDateTime;
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
		CartMenu cartMenu = new CartMenu();
		cartMenu.setCart(cart);
		cartMenu.setStoreMenu(storeMenu);
		cartMenu.setMenuCount(count);

		return cartMenu;
	}

	public void addCount(int count) {
		this.menuCount += count;
	}

}
