package com.example.smartorder.service.admin;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_MENU_NAME_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_MENU;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.MenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.model.AdminMenu;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.service.admin.Impl.AdminMenuServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class AdminMenuServiceImplTest {

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private StoreMenuRepository storeMenuRepository;

	@InjectMocks
	private AdminMenuServiceImpl menuService;

	/**
	 * ?????? ????????? ??????
	 */
	@Test
	@DisplayName("?????? ????????? ?????? ?????? - ????????????(???????????? id ?????? ?????? ????????? ???)")
	void listSuccess_categoryIdNull() {
		// given
		List<Menu> menuList = Arrays.asList(
			Menu.builder()
				.category(Category.builder().categoryName("??????").build())
				.menuName("??????").build(),
			Menu.builder()
				.category(Category.builder().categoryName("?????????").build())
				.menuName("??????").build()
		);
		Page<Menu> menus = new PageImpl<>(menuList);

		given(menuRepository.findAll(any(Pageable.class)))
			.willReturn(menus);

		// when
		Page<MenuDto> menuDtos = menuService.list(null, Pageable.unpaged());

		// then
		assertEquals("??????", menuDtos.getContent().get(0).getCategoryName());
		assertEquals("?????????", menuDtos.getContent().get(1).getCategoryName());
		assertEquals("??????", menuDtos.getContent().get(0).getMenuName());
		assertEquals("??????", menuDtos.getContent().get(1).getMenuName());
		assertEquals(2, menuDtos.getTotalElements());
		assertEquals(1, menuDtos.getTotalPages());
		assertEquals(0, menuDtos.getNumber());

	}

	@Test
	@DisplayName("?????? ????????? ?????? ?????? - ???????????? ??? ?????? ??????(???????????? id ?????? ????????? ???)")
	void listSuccess_categoryIdNotNull() {
		// given
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		List<Menu> menuList = Arrays.asList(
			Menu.builder()
				.category(category)
				.menuName("???????????????").build(),
			Menu.builder()
				.category(category)
				.menuName("??????").build()
		);
		Page<Menu> menus = new PageImpl<>(menuList);

		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(menuRepository.findAllByCategoryId(anyLong(), any(Pageable.class)))
			.willReturn(menus);

		// when
		Page<MenuDto> menuDtos = menuService.list(1L, Pageable.unpaged());

		// then
		assertEquals("??????", menuDtos.getContent().get(0).getCategoryName());
		assertEquals("??????", menuDtos.getContent().get(1).getCategoryName());
		assertEquals("???????????????", menuDtos.getContent().get(0).getMenuName());
		assertEquals("??????", menuDtos.getContent().get(1).getMenuName());
		assertEquals(2, menuDtos.getTotalElements());
		assertEquals(1, menuDtos.getTotalPages());
		assertEquals(0, menuDtos.getNumber());
	}

	@Test
	@DisplayName("?????? ????????? ?????? ?????? - ?????? ???????????? ??????")
	void listFail_notFoundCategory() {
		// given
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> menuService.list(1L, Pageable.unpaged()));

		// then
		assertEquals(NOT_FOUND_CATEGORY, exception.getErrorCode());
	}

	@Test
	@DisplayName("?????? ?????? ??????")
	void addSuccess() {
		// given
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		AdminMenu.Add req = AdminMenu.Add.builder()
			.categoryId(1L)
			.menuName("???????????????")
			.menuPrice(4500)
			.description("????????? ?????????.")
			.build();

		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(menuRepository.existsByMenuName(anyString()))
			.willReturn(false);
		ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);

		// when
		menuService.add(req);

		// then
		verify(menuRepository, times(1)).save(captor.capture());
		assertEquals("??????", captor.getValue().getCategory().getCategoryName());
		assertEquals("???????????????", captor.getValue().getMenuName());

	}

	@Test
	@DisplayName("?????? ?????? ?????? - ?????? ???????????? ??????")
	void addFail_notFoundCategory() {
		// given
		AdminMenu.Add req = AdminMenu.Add.builder()
			.categoryId(1L)
			.menuName("???????????????")
			.menuPrice(4500)
			.description("????????? ?????????.")
			.build();

		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> menuService.add(req));

		// then
		assertEquals(NOT_FOUND_CATEGORY, exception.getErrorCode());

	}

	@Test
	@DisplayName("?????? ?????? ?????? - ????????? ????????? ??????")
	void addFail_alreadyMenuNameExists() {
		// given
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		AdminMenu.Add req = AdminMenu.Add.builder()
			.categoryId(1L)
			.menuName("???????????????")
			.menuPrice(4500)
			.description("????????? ?????????.")
			.build();

		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(menuRepository.existsByMenuName(anyString()))
			.willReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> menuService.add(req));

		// then
		assertEquals(ALREADY_MENU_NAME_EXISTS, exception.getErrorCode());

	}

	@Test
	@DisplayName("?????? ?????? ??????")
	void updateSuccess() {
		// given
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		Menu menu = Menu.builder()
			.category(category)
			.menuName("???????????????")
			.menuPrice(4000).build();
		AdminMenu.Add req = AdminMenu.Add.builder()
			.categoryId(1L)
			.menuName("??????")
			.menuPrice(4500)
			.description("????????? ?????????.")
			.build();

		given(menuRepository.findById(anyLong()))
			.willReturn(Optional.of(menu));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(menuRepository.existsByMenuNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.empty());
		ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);

		// when
		menuService.update(100L, req);

		// then
		verify(menuRepository, times(1)).save(captor.capture());
		assertEquals("??????", captor.getValue().getCategory().getCategoryName());
		assertEquals("??????", captor.getValue().getMenuName());
		assertEquals(4500, captor.getValue().getMenuPrice());
	}

	@Test
	@DisplayName("?????? ?????? ?????? - ?????? ?????? ??????")
	void updateFail_notFoundMenu() {
		// given
		AdminMenu.Add req = AdminMenu.Add.builder()
			.menuName("??????")
			.build();

		given(menuRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> menuService.update(1L, req));

		// then
		assertEquals(NOT_FOUND_MENU, exception.getErrorCode());
	}

	@Test
	@DisplayName("?????? ?????? ?????? - ?????? ???????????? ??????")
	void updateFail_notFoundCategory() {
		// given
		Menu menu = Menu.builder()
			.menuName("???????????????").build();
		AdminMenu.Add req = AdminMenu.Add.builder()
			.categoryId(1L)
			.menuName("??????").build();

		given(menuRepository.findById(anyLong()))
			.willReturn(Optional.of(menu));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> menuService.update(1L, req));

		// then
		assertEquals(NOT_FOUND_CATEGORY, exception.getErrorCode());
	}

	@Test
	@DisplayName("?????? ?????? ?????? - ????????? ????????? ??????")
	void updateFail_alreadyMenuNameExists() {
		// given
		Category category = Category.builder()
			.categoryName("??????")
			.build();
		Menu menu = Menu.builder()
			.menuName("???????????????").build();
		Menu menu2 = Menu.builder()
			.menuName("??????").build();
		AdminMenu.Add req = AdminMenu.Add.builder()
			.categoryId(1L)
			.menuName("??????").build();

		given(menuRepository.findById(anyLong()))
			.willReturn(Optional.of(menu));
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(menuRepository.existsByMenuNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.of(menu2));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> menuService.update(1L, req));

		// then
		assertEquals(ALREADY_MENU_NAME_EXISTS, exception.getErrorCode());
	}


	@Test
	@DisplayName("?????? ?????? ??????")
	void delSuccess() {
		// given
		List<Long> req = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

		ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);

		// when
		menuService.del(req);

		// then
		verify(menuRepository, times(1))
			.deleteAllByMenuIdIn(captor.capture());
	}

}