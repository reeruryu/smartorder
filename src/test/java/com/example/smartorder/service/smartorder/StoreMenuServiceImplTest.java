package com.example.smartorder.service.smartorder;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.SaleState.ON_SALE;
import static com.example.smartorder.type.SaleState.SOLDOUT;
import static com.example.smartorder.type.SaleState.SOLDOUT_FOR_ONE_DAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.FrontStoreMenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.Impl.StoreMenuServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class StoreMenuServiceImplTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private StoreMenuRepository storeMenuRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private StoreMenuServiceImpl storeMenuService;

	/**
	 * ????????? ????????? ???????????????.
	 */
	@Test
	@DisplayName("?????? ????????? ?????? ?????? - ????????????(???????????? id ?????? ?????? ????????? ???)")
	void listSuccess_categoryIdNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		List<StoreMenu> menuList = Arrays.asList(
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().menuName("???????????????").build())
				.build(),
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().menuName("???????????????").build())
				.build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeMenuRepository.findAllByStore(any()))
			.willReturn(menuList);

		// when
		List<StoreMenuDto> storeMenuList =
			storeMenuService.list(1L, null, "ceo@gmail.com");

		// then
		assertEquals("???????????????", storeMenuList.get(0).getMenuName());
		assertEquals("???????????????", storeMenuList.get(1).getMenuName());
	}

	@Test
	@DisplayName("????????? ?????? ?????? ?????? - ???????????? ??? ?????? ??????(???????????? id ?????? ????????? ???)")
	void listSuccess_categoryIdNotNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		List<StoreMenu> menuList = Arrays.asList(
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().category(category).menuName("???????????????").build())
				.build(),
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().category(category).menuName("??????").build())
				.build()
		);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(storeMenuRepository.findAllByCategoryIdAndStoreId(anyLong(), any()))
			.willReturn(menuList);

		// when
		List<StoreMenuDto> storeMenuList =
			storeMenuService.list(1L, 2L, "ceo@gmail.com");

		// then
		assertEquals("???????????????", storeMenuList.get(0).getMenuName());
		assertEquals("??????", storeMenuList.get(1).getMenuName());
	}

	@Test
	@DisplayName("????????? ?????? ?????? ?????? - ???????????? ?????? ??????")
	void listFail_notFoundUser() {
		// given
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.list(1L, 2L, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());

	}

	@Test
	@DisplayName("????????? ?????? ?????? ?????? - ???????????? ?????? ??????")
	void listFail_notFoundStore() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.list(1L, 2L, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_STORE, exception.getErrorCode());

	}

	@Test
	@DisplayName("????????? ?????? ?????? ?????? - ?????? ????????? ????????? ?????? ?????? ?????????")
	void listFail_cannotAccessStore() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo1@gmail.com").build();
		Member member2 = Member.builder()
			.id(2L).userId("ceo2@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member2).build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.list(1L, 2L, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_ACCESS_STORE, exception.getErrorCode());
	}

	@Test
	@DisplayName("????????? ?????? ?????? ?????? - ???????????? ?????? ????????????")
	void listFail_notFoundCategory() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.list(1L, 2L, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_CATEGORY, exception.getErrorCode());
	}

	/**
	 * ?????? ?????? ????????? ???????????????.
	 */
	@Test
	@DisplayName("?????? ???????????? ?????? ??????")
	void updateHiddenSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(100L).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		ArgumentCaptor<StoreMenu> captor = ArgumentCaptor.forClass(StoreMenu.class);

		// when
		storeMenuService.updateHidden(
			1L, 2L, true, "ceo@gmail.com");

		// then
		verify(storeMenuRepository, times(1)).save(captor.capture());
		assertTrue(captor.getValue().isHidden());
	}

	@Test
	@DisplayName("?????? ???????????? ?????? ?????? - ???????????? ?????? ?????? ??????")
	void updateHiddenFail_notFoundStoreMenu() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.updateHidden(
				1L, 2L, true, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_STOREMENU, exception.getErrorCode());
	}

	/**
	 * ?????? ?????? ????????? ???????????????.
	 */
	@Test
	@DisplayName("?????? ?????? ?????? ?????? ??????")
	void updateSaleStateSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		StoreMenu storeMenu = StoreMenu.builder()
			.id(100L).saleState(ON_SALE).soldOutDt(null).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.of(storeMenu));
		ArgumentCaptor<StoreMenu> captor = ArgumentCaptor.forClass(StoreMenu.class);

		// when
		storeMenuService.updateSaleState(
			1L, 2L, SOLDOUT_FOR_ONE_DAY, "ceo@gmail.com");

		// then
		verify(storeMenuRepository, times(1)).save(captor.capture());
		assertEquals(SOLDOUT_FOR_ONE_DAY, captor.getValue().getSaleState());
		assertNotNull(captor.getValue().getSoldOutDt());
	}

	@Test
	@DisplayName("?????? ?????? ?????? ?????? ??????")
	void updateSaleStateFail_notFoundStoreMenu() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeMenuRepository.findById(anyLong()))
			.willReturn(Optional.empty());
		ArgumentCaptor<StoreMenu> captor = ArgumentCaptor.forClass(StoreMenu.class);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.updateHidden(
				1L, 2L, true, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_STOREMENU, exception.getErrorCode());
	}

	/**
	 *  ???????????? ?????? ????????? ???????????????.
	 */
	@Test
	@DisplayName("?????? ?????? ????????? ?????? ?????? - ????????????(???????????? id ?????? ?????? ????????? ???)")
	void frontSuccess_categoryIdNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("user@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		List<StoreMenu> storeMenuList = Arrays.asList(
			StoreMenu.builder()
				.id(1L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("???????????????").menuPrice(4500).build())
				.build(),
			StoreMenu.builder()
				.id(2L).saleState(SOLDOUT)
				.menu(Menu.builder().menuName("???????????????").menuPrice(5800).build())
				.build(),
			StoreMenu.builder()
				.id(3L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("???????????????").menuPrice(5500).build())
				.build()
		);
		Page<StoreMenu> storeMenus = new PageImpl<>(storeMenuList);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeMenuRepository.findByStoreIdExceptHiddenTrue(anyLong(), any()))
			.willReturn(storeMenus);

		// when
		Page<FrontStoreMenuDto> storeMenuDtos =
			storeMenuService.front(1L, null, Pageable.unpaged(), "user@naver.com");

		// then
		assertEquals(3, storeMenuDtos.getTotalElements());
	}

	@Test
	@DisplayName("?????? ?????? ????????? ?????? ?????? - ???????????? ??? ?????? ??????(???????????? id ?????? ????????? ???)")
	void frontSuccess_categoryIdNotNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("user@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		List<StoreMenu> storeMenuList = Arrays.asList(
			StoreMenu.builder()
				.id(1L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("???????????????").menuPrice(4500).build())
				.build(),
			StoreMenu.builder()
				.id(2L).saleState(SOLDOUT)
				.menu(Menu.builder().menuName("????????????").menuPrice(5100).build())
				.build(),
			StoreMenu.builder()
				.id(3L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("???????????????").menuPrice(5800).build())
				.build()
		);
		Page<StoreMenu> storeMenus = new PageImpl<>(storeMenuList);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(storeMenuRepository.findByCategoryIdAndStoreIdExceptHiddenTrue(anyLong(), anyLong(), any()))
			.willReturn(storeMenus);

		// when
		Page<FrontStoreMenuDto> storeMenuDtos =
			storeMenuService.front(1L, 1L, Pageable.unpaged(), "user@naver.com");

		// then
		assertEquals(3, storeMenuDtos.getTotalElements());
	}

	@Test
	@DisplayName("?????? ?????? ????????? ?????? ??????")
	void frontFail() {
		// given
		Member member = Member.builder()
			.id(1L).userId("user@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeMenuService.front(1L, 1L, Pageable.unpaged(), "user@naver.com"));

		// then
		assertEquals(NOT_FOUND_CATEGORY, exception.getErrorCode());
	}

}
