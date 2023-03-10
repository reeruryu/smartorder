package com.example.smartorder.service.smartorder;

import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.model.CartParam;
import java.util.List;

public interface CartService {


	/**
	 * 장바구니에 메뉴를 추가합니다.
	 */
	void addCartMenu(CartParam.Add parameter, String userId);

	/**
	 * 장바구니에 있는 메뉴를 보여줍니다.
	 */
	List<CartMenuDto> getCartMenuList(String userId);

	/**
	 * 장바구니에 담긴 메뉴 수량을 변경합니다.
	 */
	void updateCartMenu(CartParam.Update parameter, String userId);

	/**
	 * 장바구니에 담긴 메뉴를 삭제합니다.
	 */
	void deleteCartMenu(Long cartMenuId, String userId);

	/**
	 * 장바구니에 담긴 메뉴들을 주문합니다.
	 */
	Long orderCartMenu(Long cartId, String userId);
}
