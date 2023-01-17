package com.example.smartorder.service.smartorder;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_CART;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_BUY_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.CART_EMPTY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CARTMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.NOT_SAME_STORE;
import static com.example.smartorder.common.error.ErrorCode.STORE_NOT_OPEN;
import static com.example.smartorder.type.SaleState.ON_SALE;
import static com.example.smartorder.type.SaleState.SOLDOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.CartMenuDto;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.entity.Cart;
import com.example.smartorder.entity.CartMenu;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.model.CartParam;
import com.example.smartorder.repository.CartMenuRepository;
import com.example.smartorder.repository.CartRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.Impl.CartServiceImpl;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

	@Mock
	private CartMenuRepository cartMenuRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private StoreMenuRepository storeMenuRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OrderService orderService;

	@InjectMocks
	private CartServiceImpl cartService;

	/**
	 * 장바구니에 메뉴를 추가합니다.
	 */
	@Test
	@DisplayName("장바구니에 메뉴 담기 성공 - 장바구니를 담아본 경험 없음")
	void addCartMenuSuccess_cartNull() {
		// given
		CartParam.Add req = CartParam.Add.builder()
			.storeMenuId(1L)
			.menuCount(2).build();
		Member member = Member.builder()
			.id(1L).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(1L).hidden(false).saleState(ON_SALE).build();
		List<CartMenu> cartMenuList = new ArrayList<>();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(null);
		ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);

		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(cartMenuRepository.findByCartAndStoreMenu(any(), any()))
			.willReturn(null);
		ArgumentCaptor<CartMenu> captor2 = ArgumentCaptor.forClass(CartMenu.class);

		// when
		cartService.addCartMenu(req, "user@gmail.com");

		// then
		verify(cartRepository, times(1)).save(captor.capture());
		verify(cartMenuRepository, times(1)).save(captor2.capture());
		assertEquals(2, captor2.getValue().getMenuCount());
	}

	@Test
	@DisplayName("장바구니에 메뉴 담기 성공 "
		+ "- 장바구니를 이미 담아본 경험이 있고 장바구니가 비어 있음")
	void addCartMenuSuccess_cartNotNull() {
		// given
		CartParam.Add req = CartParam.Add.builder()
			.storeMenuId(1L)
			.menuCount(2).build();
		Member member = Member.builder()
			.id(1L).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(1L).hidden(false).saleState(ON_SALE).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = new ArrayList<>();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(cartMenuRepository.findByCartAndStoreMenu(any(), any()))
			.willReturn(null);
		ArgumentCaptor<CartMenu> captor = ArgumentCaptor.forClass(CartMenu.class);

		// when
		cartService.addCartMenu(req, "user@gmail.com");

		// then
		verify(cartMenuRepository, times(1)).save(captor.capture());
		assertEquals(2, captor.getValue().getMenuCount());

	}

	@Test
	@DisplayName("장바구니에 메뉴 담기 성공 "
		+ "- 장바구니에 이미 같은 메뉴가 담겨 있음(수량 추가)")
	void addCartMenuSuccess_cartNotNull_cartMenuNotNull() {
		// given
		CartParam.Add req = CartParam.Add.builder()
			.storeMenuId(100L)
			.menuCount(2).build();
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(100L).store(store).hidden(false).saleState(ON_SALE).build();
		Cart cart = Cart.builder()
				.member(member).build();
		CartMenu cartMenu = CartMenu.builder()
			.id(1L).menuCount(3).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(100L).storeMenu(storeMenu).menuCount(1).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(cartMenuRepository.findByCartAndStoreMenu(any(), any()))
			.willReturn(cartMenu);
		ArgumentCaptor<CartMenu> captor = ArgumentCaptor.forClass(CartMenu.class);

		// when
		cartService.addCartMenu(req, "user@gmail.com");

		// then
		verify(cartMenuRepository, times(1)).save(captor.capture());
		assertEquals(5, captor.getValue().getMenuCount());

	}

	@Test
	@DisplayName("장바구니에 메뉴 담기 실패 - 가게 메뉴가 품절임")
	void addCartMenuSuccess_cannotBuyStoreMenu() {
		// given
		CartParam.Add req = CartParam.Add.builder()
			.storeMenuId(100L)
			.menuCount(2).build();
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(100L).store(store).hidden(false).saleState(SOLDOUT).build();
		Cart cart = Cart.builder()
			.member(member).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.addCartMenu(req, "user@gmail.com"));

		// then
		assertEquals(CANNOT_BUY_STOREMENU, exception.getErrorCode());

	}

	@Test
	@DisplayName("장바구니에 메뉴 담기 실패 - 장바구니에 원래 담긴 메뉴와 매장이 다름")
	void addCartMenuSuccess_notSameStore() {
		// given
		CartParam.Add req = CartParam.Add.builder()
			.storeMenuId(100L)
			.menuCount(2).build();
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).build();
		Store store2 = Store.builder()
			.id(2L).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(100L).store(store).hidden(false).saleState(ON_SALE).build();
		StoreMenu storeMenu2 = StoreMenu.builder()
			.id(200L).store(store2).hidden(false).saleState(ON_SALE).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(100L).storeMenu(storeMenu2).menuCount(1).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(cartMenuRepository.findAllByCart(cart))
			.willReturn(cartMenuList);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.addCartMenu(req, "user@gmail.com"));

		// then
		assertEquals(NOT_SAME_STORE, exception.getErrorCode());

	}

	/**
	 * 장바구니 메뉴 조회
	 */
	@Test
	@DisplayName("장바구니 메뉴 조회 성공")
	void getCartMenuListSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(1L).menuCount(2)
				.storeMenu(StoreMenu.builder()
					.menu(Menu.builder().menuName("라떼").menuPrice(5100).build())
					.store(Store.builder().id(1L).build()).build()).build(),
			CartMenu.builder()
				.id(2L).menuCount(1)
				.storeMenu(StoreMenu.builder()
					.menu(Menu.builder().menuName("쿠키").menuPrice(4800).build())
					.store(Store.builder().id(1L).build()).build()).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);

		// when
		List<CartMenuDto> cartMenuDtoList =
			cartService.getCartMenuList("user@gmail.com");

		// then
		assertEquals("라떼", cartMenuDtoList.get(0).getMenuName());
		assertEquals("쿠키", cartMenuDtoList.get(1).getMenuName());
		assertEquals(2, cartMenuDtoList.size());

	}

	@Test
	@DisplayName("장바구니 메뉴 조회 성공 - 장바구니를 담아본 경험 없음 (빈 장바구니)")
	void getCartMenuListSuccess_cartNull() {
		// given
		Member member = Member.builder()
			.id(1L).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(null);

		// when
		List<CartMenuDto> cartMenuDtoList =
			cartService.getCartMenuList("user@gmail.com");

		// then
		assertEquals(0, cartMenuDtoList.size());

	}

	@Test
	@DisplayName("장바구니 메뉴 조회 실패 - 존재하지 않는 유저")
	void getCartMenuListFail_notFoundUser() {
		// given
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.getCartMenuList("user@gmail.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());

	}

	/**
	 * 장바구니 수량 변경
	 */
	@Test
	@DisplayName("장바구니 메뉴 수량 수정 성공")
	void updateCartMenuSuccess() {
		// given
		CartParam.Update req = CartParam.Update.builder()
			.cartMenuId(1L).count(4).build();
		Member member = Member.builder()
			.id(1L).build();
		Cart cart = Cart.builder()
			.member(member).build();
		CartMenu cartMenu = CartMenu.builder()
			.cart(cart).menuCount(2).build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(cartMenu));
		ArgumentCaptor<CartMenu> captor = ArgumentCaptor.forClass(CartMenu.class);

		// when
		cartService.updateCartMenu(req, "user@gmail.com");

		// then
		verify(cartMenuRepository, times(1)).save(captor.capture());
		assertEquals(4, captor.getValue().getMenuCount());

	}

	@Test
	@DisplayName("장바구니 메뉴 수량 수정 실패 - 존재하지 않는 장바구니 메뉴")
	void updateCartMenuFail_notFoundCartMenu() {
		// given
		CartParam.Update req = CartParam.Update.builder()
			.cartMenuId(1L).count(4).build();
		Member member = Member.builder()
			.id(1L).build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartMenuRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.updateCartMenu(req, "user@gmail.com"));

		// then
		assertEquals(NOT_FOUND_CARTMENU, exception.getErrorCode());

	}

	@Test
	@DisplayName("장바구니 메뉴 수량 수정 실패 - 해당 유저의 장바구니가 아님")
	void updateCartMenuFail_cannotAccessCart() {
		// given
		CartParam.Update req = CartParam.Update.builder()
			.cartMenuId(1L).count(4).build();
		Member member = Member.builder()
			.id(1L).build();
		Member member2 = Member.builder()
			.id(2L).build();
		Cart cart = Cart.builder()
			.member(member2).build();
		CartMenu cartMenu = CartMenu.builder()
			.cart(cart).menuCount(2).build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(cartMenu));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.updateCartMenu(req, "user@gmail.com"));

		// then
		assertEquals(CANNOT_ACCESS_CART, exception.getErrorCode());

	}

	/**
	 * 장바구니 메뉴 삭제
	 */
	@Test
	@DisplayName("장바구니 메뉴 삭제 성공")
	void deleteCartMenuSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Cart cart = Cart.builder()
			.member(member).build();
		CartMenu cartMenu = CartMenu.builder()
			.cart(cart).menuCount(2).build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(cartMenu));
		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

		// when
		cartService.deleteCartMenu(1L, "user@gmail.com");

		// then
		verify(cartMenuRepository, times(1)).deleteById(captor.capture());
	}

	/**
	 * 장바구니 메뉴 주문
	 */
	@Test
	@DisplayName("장바구니 메뉴 주문 성공")
	void orderCartMenuSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).open(true).openDay("0,1,2,3,4,5,6,7")
			.startTime(LocalTime.MIN).endTime(LocalTime.MAX)
			.build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(1L).hidden(false).saleState(ON_SALE)
			.menu(Menu.builder().menuName("라떼").menuPrice(5100).build())
			.store(store).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(1L).menuCount(2)
				.storeMenu(storeMenu).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		ArgumentCaptor<OrderDto> captor = ArgumentCaptor.forClass(OrderDto.class);
		ArgumentCaptor<CartMenu> captor2 = ArgumentCaptor.forClass(CartMenu.class);

		// when
		Long orderId = cartService.orderCartMenu(1L, "user@naver.com");

		// then
		assertNotNull(orderId);
		verify(orderService, times(1)).order(captor.capture());
		verify(cartMenuRepository, times(1)).delete(captor2.capture());
		assertEquals(10200, captor.getValue().getOrderPrice());
		assertEquals(2, captor2.getValue().getMenuCount());
	}

	@Test
	@DisplayName("장바구니 메뉴 주문 실패 - 빈 장바구니")
	void orderCartMenuFail_cartEmpty() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = new ArrayList<>();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.orderCartMenu(1L, "user@naver.com"));

		// then
		assertEquals(CART_EMPTY, exception.getErrorCode());
	}

	@Test
	@DisplayName("장바구니 메뉴 주문 실패 - 가게 임시 영업 중단")
	void orderCartMenuFail_storeNotOpen() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).open(false).openDay("1,2,3,4,5,6,7")
			.startTime(LocalTime.MIN).endTime(LocalTime.MAX)
			.build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(1L).hidden(false).saleState(ON_SALE)
			.menu(Menu.builder().menuName("라떼").menuPrice(5100).build())
			.store(store).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(1L).menuCount(2)
				.storeMenu(storeMenu).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.orderCartMenu(1L, "user@naver.com"));

		// then
		assertEquals(STORE_NOT_OPEN, exception.getErrorCode());
	}

	@Test
	@DisplayName("장바구니 메뉴 주문 실패 - 가게 오픈 요일 아님")
	void orderCartMenuFail_storeNotOpen2() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).open(false).openDay("")
			.startTime(LocalTime.MIN).endTime(LocalTime.MAX)
			.build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(1L).hidden(false).saleState(ON_SALE)
			.menu(Menu.builder().menuName("라떼").menuPrice(5100).build())
			.store(store).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(1L).menuCount(2)
				.storeMenu(storeMenu).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.orderCartMenu(1L, "user@naver.com"));

		// then
		assertEquals(STORE_NOT_OPEN, exception.getErrorCode());
	}

	@Test
	@DisplayName("장바구니 메뉴 주문 실패 - 오픈 시간이 아님")
	void orderCartMenuFail_storeNotOpen3() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).open(false).openDay("1,2,3,4,5,6,7")
			.startTime(LocalTime.now().minusHours(2))
			.endTime(LocalTime.now().minusHours(1))
			.build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(1L).hidden(false).saleState(ON_SALE)
			.menu(Menu.builder().menuName("라떼").menuPrice(5100).build())
			.store(store).build();
		Cart cart = Cart.builder()
			.member(member).build();
		List<CartMenu> cartMenuList = Arrays.asList(
			CartMenu.builder()
				.id(1L).menuCount(2)
				.storeMenu(storeMenu).build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(cartRepository.findByMember(any()))
			.willReturn(cart);
		given(cartMenuRepository.findAllByCart(any()))
			.willReturn(cartMenuList);
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.orderCartMenu(1L, "user@naver.com"));

		// then
		assertEquals(STORE_NOT_OPEN, exception.getErrorCode());
	}
}