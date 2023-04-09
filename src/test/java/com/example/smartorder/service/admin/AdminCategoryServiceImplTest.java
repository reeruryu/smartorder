package com.example.smartorder.service.admin;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_CATEGORY_NAME_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.CategoryDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.model.AdminCategory;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.service.admin.impl.AdminCategoryServiceImpl;
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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private MenuRepository menuRepository;
	@InjectMocks
	private AdminCategoryServiceImpl categoryService;

	/**
	 * 카테고리 목록 보기 테스트
	 */
	@Test
	@DisplayName("카테고리 목록 보기")
	void list() {
		// given
		List<Category> categoryList = Arrays.asList(
			Category.builder()
				.categoryName("커피")
				.build(),
			Category.builder()
				.categoryName("음료")
				.build(),
			Category.builder()
				.categoryName("디저트")
				.build()
		);

		given(categoryRepository.findAll(any(Sort.class)))
			.willReturn(categoryList);

		// when
		List<CategoryDto> categoryDtoList = categoryService.list();

		// then
		assertEquals(3, categoryDtoList.size());
		assertEquals("커피", categoryDtoList.get(0).getCategoryName());
		assertEquals("음료", categoryDtoList.get(1).getCategoryName());
		assertEquals("디저트", categoryDtoList.get(2).getCategoryName());

	}

	/**
	 * 카테고리 추가 테스트
	 */
	@Test
	@DisplayName("카테고리 추가 성공")
	void addSuccess() {
		// given
		AdminCategory.Add req = AdminCategory.Add.builder()
			.categoryName("커피").build();

		given(categoryRepository.findByCategoryName(anyString()))
			.willReturn(Optional.empty());
		ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

		// when
		categoryService.add(req);

		// then
		verify(categoryRepository, times(1)).save(captor.capture());
		assertEquals(req.getCategoryName(), captor.getValue().getCategoryName());
		assertEquals(0, captor.getValue().getSortValue()); // default

	}

	@Test
	@DisplayName("카테고리 추가 실패 - 중복된 카테고리명 존재")
	void addFail_alreadyCategoryNameExists() {
		// given
		AdminCategory.Add req = AdminCategory.Add.builder()
			.categoryName("커피").build();
		Category category = Category.builder()
			.categoryName("커피")
			.build();

		given(categoryRepository.findByCategoryName(anyString()))
			.willReturn(Optional.of(category));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> categoryService.add(req));

		// then
		assertEquals(ALREADY_CATEGORY_NAME_EXISTS, exception.getErrorCode());

	}

	/**
	 * 카테고리 수정 테스트
	 */
	@Test
	@DisplayName("카테고리 수정 성공")
	void updateSuccess() {
		// given
		Category category = Category.builder()
			.categoryName("커피")
			.sortValue(0)
			.build();

		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(categoryRepository.existsByCategoryNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.empty());
		ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);

		// when
		categoryService.update(1L, "음료", 2);

		// then
		verify(categoryRepository, times(1)).save(captor.capture());
		assertEquals("음료", captor.getValue().getCategoryName());
		assertEquals(2, captor.getValue().getSortValue());

	}

	@Test
	@DisplayName("카테고리 수정 실패 - 해당 카테고리 없음")
	void updateFail_notFoundCategory() {
		// given
		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> categoryService.update(1L, "프라페", 2));

		// then
		assertEquals(NOT_FOUND_CATEGORY, exception.getErrorCode());

	}

	@Test
	@DisplayName("카테고리 수정 실패 - 중복된 카테고리명 존재")
	void updateFail_alreadyCategoryNameExists() {
		// given
		Category category = Category.builder()
			.id(1L)
			.categoryName("커피")
			.sortValue(0)
			.build();
		Category category2 = Category.builder()
			.id(2L)
			.categoryName("음료")
			.sortValue(0)
			.build();

		given(categoryRepository.findById(anyLong()))
			.willReturn(Optional.of(category));
		given(categoryRepository.existsByCategoryNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.of(category2));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> categoryService.update(1L, "음료", 2));

		// then
		assertEquals(ALREADY_CATEGORY_NAME_EXISTS, exception.getErrorCode());

	}

	/**
	 * 카테고리 삭제 테스트
	 */
	@Test
	@DisplayName("카테고리 삭제 성공")
	void delSuccess() {
		// given
		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

		// when
		categoryService.del(1L);

		// then
		verify(categoryRepository, times(1))
			.deleteByCategoryId(captor.capture());
	}

}