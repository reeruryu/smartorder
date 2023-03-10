package com.example.smartorder.service.smartorder.Impl;

import static com.example.smartorder.common.error.ErrorCode.*;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_CART;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_BUY_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.CART_EMPTY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CART;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CARTMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.STORE_NOT_OPEN;
import static com.example.smartorder.type.SaleState.ON_SALE;

import com.example.smartorder.common.error.ErrorCode;
import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderMenuDto;
import com.example.smartorder.entity.Cart;
import com.example.smartorder.entity.CartMenu;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.model.CartParam;
import com.example.smartorder.repository.CartMenuRepository;
import com.example.smartorder.repository.CartRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.CartService;
import com.example.smartorder.service.smartorder.OrderService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartServiceImpl implements CartService {
	private final CartMenuRepository cartMenuRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final StoreMenuRepository storeMenuRepository;
	private final StoreRepository storeRepository;

	private final OrderService orderService; // ??????

	@Override
	public void addCartMenu(CartParam.Add parameter, String userId) {

		// ?????? ?????? ???
		Member member = getMember(userId);

		// ?????? ???????????? get
		Cart cart = cartRepository.findByMember(member);
		if (cart == null) { // ???????????? ????????? ??????
			cart = Cart.createCart(member);
			cartRepository.save(cart);
		}

		// ?????? ?????? validate ??????
		StoreMenu storeMenu = validateStoreMenu(parameter.getStoreMenuId());

		// ????????? ?????? ?????? ?????? ????????????
		List<CartMenu> cartMenuList = cartMenuRepository.findAllByCart(cart);
		if (cartMenuList.size() > 0) {
			CartMenu cartOneMenu = cartMenuList.get(0);
			if (!cartOneMenu.getStoreMenu().getStore().equals(storeMenu.getStore())) {
				throw new CustomException(NOT_SAME_STORE);
			}
		}

		// ?????? ?????? get
		CartMenu cartMenu = cartMenuRepository.findByCartAndStoreMenu(cart, storeMenu);
		if (cartMenu == null) { // ?????????
			cartMenu = CartMenu.createCartMenu(cart, storeMenu, parameter.getMenuCount());
		} else { // ?????????
			cartMenu.addCount(parameter.getMenuCount());
		}
		cartMenuRepository.save(cartMenu);

	}

	@Override
	public List<CartMenuDto> getCartMenuList(String userId) {

		List<CartMenuDto> cartMenuList = new ArrayList<>();

		// ?????? ?????? ???
		Member member = getMember(userId);

		Cart cart = cartRepository.findByMember(member);
		if (cart == null) { // ?????????
			return cartMenuList; // ??? ????????? return
		}

		return cartMenuRepository.findAllByCart(cart)
			.stream().map(CartMenuDto::of).collect(Collectors.toList());
	}

	@Override
	public void updateCartMenu(CartParam.Update parameter, String userId) {

		// ?????? ?????? ???
		Member member = getMember(userId);

		CartMenu cartMenu = validateCartMenu(parameter.getCartMenuId(), member);

		cartMenu.setMenuCount(parameter.getCount());
		cartMenuRepository.save(cartMenu);

	}

	@Override
	public void deleteCartMenu(Long cartMenuId, String userId) {
		// ?????? ?????? ???
		Member member = getMember(userId);

		CartMenu cartMenu = validateCartMenu(cartMenuId, member);

		cartMenuRepository.deleteById(cartMenuId);

	}

	@Override
	public Long orderCartMenu(Long cartId, String userId) {

		Member member = getMember(userId);

		Cart cart = cartRepository.findByMember(member);
		if (cart == null) {
			throw new CustomException(NOT_FOUND_CART);
		}

		// ?????? ??????
		List<CartMenu> cartMenuList = cartMenuRepository.findAllByCart(cart);
		if (CollectionUtils.isEmpty(cartMenuList)) {
			throw new CustomException(CART_EMPTY);
		}

		// ?????? ??????
		List<OrderMenuDto> orderMenu = new ArrayList<>();
		long totalPrice = 0;
		StoreMenu storeMenu = new StoreMenu();
		for (CartMenu cartMenu: cartMenuList) {
			storeMenu = validateStoreMenu(cartMenu.getStoreMenu().getId());

			long menuPrice = storeMenu.getMenu().getMenuPrice();
			totalPrice += (menuPrice * cartMenu.getMenuCount());

			orderMenu.add(OrderMenuDto.builder()
									.menuName(storeMenu.getMenu().getMenuName())
									.menuPrice(menuPrice)
									.menuCount(cartMenu.getMenuCount())
									.build());
		}

		// ??????
		Store store = validateStore(storeMenu.getStore().getId());

		// ????????????
		Long orderId = orderService.order(OrderDto.builder()
													.member(member)
													.store(store)
													.orderMenu(orderMenu)
													.orderPrice(totalPrice)
													.build());

		// ???????????? ?????? ??????
		for (CartMenu cartMenu: cartMenuList) {
			cartMenuRepository.delete(cartMenu);
		}

		return orderId;
	}

	private Member getMember(String userId) {
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		return member;
	}

	private StoreMenu validateStoreMenu(Long storeMenuId) {
		// ?????? ?????? ?????? ???
		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STOREMENU));

		// ?????? ????????? ????????? ???????????? ???
		if (storeMenu.isHidden() || ON_SALE != storeMenu.getSaleState()) {
			throw new CustomException(CANNOT_BUY_STOREMENU);
		}

		return storeMenu;
	}

	private CartMenu validateCartMenu(Long cartMenuId, Member member) {
		// ?????? ?????? ?????????
		CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_CARTMENU));

		// ????????? ??????????????????
		if (!member.equals(cartMenu.getCart().getMember())) {
			throw new CustomException(CANNOT_ACCESS_CART);
		}
		return cartMenu;
	}

	// ?????? ??? ??????  open, ?????? ??????, ?????? check
	private Store validateStore(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		if (!store.isOpen()) {
			log.info("isOpen()");
			throw new CustomException(STORE_NOT_OPEN);
		}

		if(!store.isOpenDay(LocalDateTime.now())) {
			log.info("isOpenDay()");
			throw new CustomException(STORE_NOT_OPEN);
		}

		if(!store.isOpenTime(LocalTime.now())) {
			log.info("isOpenTime()");
			throw new CustomException(STORE_NOT_OPEN);
		}

		return store;
	}

}
