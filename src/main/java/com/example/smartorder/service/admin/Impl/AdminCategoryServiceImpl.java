package com.example.smartorder.service.admin.Impl;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_CATEGORY_NAME_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;

import com.example.smartorder.common.exception.AdminException;
import com.example.smartorder.dto.CategoryDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.model.AdminCategory.Add;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.service.admin.AdminCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
	private final CategoryRepository categoryRepository;
	private final MenuRepository menuRepository;

	@Override
	public List<CategoryDto> list() {
		List<Category> list = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortValue"));

		return CategoryDto.of(list);
	}

	@Override
	public void add(Add parameter) {
		Category category = categoryRepository.findByCategoryName(parameter.getCategoryName())
			.orElseThrow(() -> new AdminException(ALREADY_CATEGORY_NAME_EXISTS));

		categoryRepository.save(parameter.toEntity());
	}

	@Override
	public void update(Long id, String categoryName, int sortValue) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new AdminException(NOT_FOUND_CATEGORY));

		category.setCategoryName(categoryName);
		category.setSortValue(sortValue);
		categoryRepository.save(category);
	}

	@Override
	public void del(Long id) {
		List<Menu> menuList = menuRepository.findAllByCategoryId(id);

		if (!CollectionUtils.isEmpty(menuList)) {
			for (Menu menu: menuList) {
				menuRepository.deleteById(menu.getId());
			}
		}
		categoryRepository.deleteById(id);

	}
}
