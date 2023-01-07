package com.example.smartorder.service.Impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_CART;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_BUY_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CARTMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.SaleState.ON_SALE;

import com.example.smartorder.common.error.ErrorCode;
import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.entity.Cart;
import com.example.smartorder.entity.CartMenu;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.mapper.CartMenuMapper;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.model.AddCartMenu;
import com.example.smartorder.model.UpdateCartMenu;
import com.example.smartorder.repository.CartMenuRepository;
import com.example.smartorder.repository.CartRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.CartService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
	private final CartMenuRepository cartMenuRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final StoreMenuRepository storeMenuRepository;
	private final StoreRepository storeRepository;

	private final CartMenuMapper cartMenuMapper;

	@Override
	public void addCartMenu(AddCartMenu parameter, String userId) { // storemenuid, menucnt

		// 유저 없을 시
		Member member = getMember(userId);

		// 유저 장바구니 get
		Cart cart = cartRepository.findByMember(member);
		if (cart == null) { // 장바구니 없으면 생성
			cart = Cart.createCart(member);
			cartRepository.save(cart);
		}

/*		// 가게 validate 검사
		Store store = storeRepository.findById(parameter.getStoreId())
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
		// 운영 중인지 검사하기*/

		// 가게 메뉴 validate 검사
		StoreMenu storeMenu = validateStoreMenu(parameter.getStoreMenuId());

		// 카트에 같은 가게 메뉴 담겼는지
		List<CartMenu> cartMenuList = cartMenuRepository.findAllByCart(cart);
		if (cartMenuList.size() > 0) {
			CartMenu cartOneMenu = cartMenuList.get(0);
			if (!cartOneMenu.getStoreMenu().getStore().equals(storeMenu.getStore())) {
				throw new NotFoundException(ErrorCode.NOT_SAME_STORE);
			}
		}

		// 카트 메뉴 get
		CartMenu cartMenu = cartMenuRepository.findByCartAndStoreMenu(cart, storeMenu);
		if (cartMenu == null) { // 없으면
			cartMenu = CartMenu.createCartMenu(cart, storeMenu, parameter.getMenuCount());
		} else { // 있으면
			cartMenu.addCount(parameter.getMenuCount());
		}
		cartMenuRepository.save(cartMenu);

	}

	@Override
	public List<CartMenuDto> getCartMenuList(String userId) {

		List<CartMenuDto> cartMenuList = new ArrayList<>();

		// 유저 없을 시
		Member member = getMember(userId);

		Cart cart = cartRepository.findByMember(member);
		if (cart == null) { // 없으면
			return cartMenuList; // 빈 리스트 return
		}

		cartMenuList = cartMenuMapper.selectList(cart.getId());
		return cartMenuList;
	}

	@Override
	public void updateCartMenu(UpdateCartMenu parameter, String userId) {

		// 유저 없을 시
		Member member = getMember(userId);

		CartMenu cartMenu = validateCartMenu(parameter.getCartMenuId(), member);

		cartMenu.setMenuCount(parameter.getCount());
		cartMenuRepository.save(cartMenu);

	}

	@Override
	public void deleteCartMenu(Long cartMenuId, String userId) {
		// 유저 없을 시
		Member member = getMember(userId);

		CartMenu cartMenu = validateCartMenu(cartMenuId, member);

		cartMenuRepository.deleteById(cartMenuId);

	}

	private Member getMember(String userId) {
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
		return member;
	}

	private StoreMenu validateStoreMenu(Long storeMenuId) {
		// 가게 메뉴 없을 시
		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STOREMENU));

		// 가게 메뉴가 구매가 불가능할 시
		if (storeMenu.isHiddenYn() || !ON_SALE.equals(storeMenu.getSaleState())) {
			throw new NotFoundException(CANNOT_BUY_STOREMENU);
		}

		return storeMenu;
	}

	private CartMenu validateCartMenu(Long cartMenuId, Member member) {
		CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_CARTMENU));

		// 유저의 장바구니인지
		if (!member.equals(cartMenu.getCart().getMember())) {
			throw new NotFoundException(CANNOT_ACCESS_CART);
		}
		return cartMenu;
	}

	// 주문 시 가게  openYn, 운영 요일, 시간 check


}
