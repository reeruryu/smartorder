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
	 * 점주가 메뉴를 조회합니다.
	 */
	@Test
	@DisplayName("메뉴 리스트 보기 성공 - 전체보기(카테고리 id 입력 받지 않았을 때)")
	void listSuccess_categoryIdNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		List<StoreMenu> menuList = Arrays.asList(
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().menuName("아메리카노").build())
				.build(),
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().menuName("얼그레이티").build())
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
		assertEquals("아메리카노", storeMenuList.get(0).getMenuName());
		assertEquals("얼그레이티", storeMenuList.get(1).getMenuName());
	}

	@Test
	@DisplayName("스토어 메뉴 보기 성공 - 카테고리 별 메뉴 보기(카테고리 id 입력 받았을 때)")
	void listSuccess_categoryIdNotNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("ceo@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		Category category = Category.builder()
			.categoryName("커피")
			.build();
		List<StoreMenu> menuList = Arrays.asList(
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().category(category).menuName("아메리카노").build())
				.build(),
			StoreMenu.builder()
				.store(store)
				.menu(Menu.builder().category(category).menuName("라떼").build())
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
		assertEquals("아메리카노", storeMenuList.get(0).getMenuName());
		assertEquals("라떼", storeMenuList.get(1).getMenuName());
	}

	@Test
	@DisplayName("스토어 메뉴 보기 실패 - 존재하지 않는 유저")
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
	@DisplayName("스토어 메뉴 보기 실패 - 존재하지 않는 매장")
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
	@DisplayName("스토어 메뉴 보기 실패 - 매장 점주와 입력한 회원 정보 불일치")
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
	@DisplayName("스토어 메뉴 보기 실패 - 존재하지 않는 카테고리")
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
	 * 메뉴 숨김 여부를 수정합니다.
	 */
	@Test
	@DisplayName("메뉴 숨김여부 변경 성공")
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
	@DisplayName("메뉴 숨김여부 변경 실패 - 존재하지 않는 매장 메뉴")
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
	 * 메뉴 판매 상태를 수정합니다.
	 */
	@Test
	@DisplayName("메뉴 판매 상태 변경 성공")
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
	@DisplayName("메뉴 판매 상태 변경 실패")
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
	 *  고객에게 가게 메뉴를 보여줍니다.
	 */
	@Test
	@DisplayName("고객 메뉴 리스트 보기 성공 - 전체보기(카테고리 id 입력 받지 않았을 때)")
	void frontSuccess_categoryIdNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("user@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		List<StoreMenu> storeMenuList = Arrays.asList(
			StoreMenu.builder()
				.id(1L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("아메리카노").menuPrice(4500).build())
				.build(),
			StoreMenu.builder()
				.id(2L).saleState(SOLDOUT)
				.menu(Menu.builder().menuName("크로크무슈").menuPrice(5800).build())
				.build(),
			StoreMenu.builder()
				.id(3L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("자몽에이드").menuPrice(5500).build())
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
	@DisplayName("고객 메뉴 리스트 보기 성공 - 카테고리 별 메뉴 보기(카테고리 id 입력 받았을 때)")
	void frontSuccess_categoryIdNotNull() {
		// given
		Member member = Member.builder()
			.id(1L).userId("user@gmail.com").build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		Category category = Category.builder()
			.categoryName("커피")
			.build();
		List<StoreMenu> storeMenuList = Arrays.asList(
			StoreMenu.builder()
				.id(1L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("아메리카노").menuPrice(4500).build())
				.build(),
			StoreMenu.builder()
				.id(2L).saleState(SOLDOUT)
				.menu(Menu.builder().menuName("카페라떼").menuPrice(5100).build())
				.build(),
			StoreMenu.builder()
				.id(3L).saleState(ON_SALE)
				.menu(Menu.builder().menuName("바닐라라떼").menuPrice(5800).build())
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
	@DisplayName("고객 메뉴 리스트 보기 실패")
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
