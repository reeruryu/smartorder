package com.example.smartorder.admin.service.Impl;

import com.example.smartorder.admin.dto.CategoryDto;
import com.example.smartorder.admin.mapper.CategoryMapper;
import com.example.smartorder.admin.model.CategoryInput;
import com.example.smartorder.category.entity.Category;
import com.example.smartorder.category.repository.CategoryRepository;
import com.example.smartorder.admin.service.AdminCategoryService;
import com.example.smartorder.menu.entity.Menu;
import com.example.smartorder.menu.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

	private final CategoryRepository categoryRepository;
	private final MenuRepository menuRepository;
//	private final CategoryMapper categoryMapper;

	@Override
	public List<CategoryDto> list() {
		List<Category> list = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortValue"));

		return CategoryDto.of(list);
	}

//	@Override
//	public List<CategoryDto> listByMenu() {
//		return categoryMapper.selectList();
//	}

	@Override
	public boolean add(CategoryInput parameter) {
		Optional<Category> optionalCategory = categoryRepository.findByCategoryName(parameter.getCategoryName());

		if (optionalCategory.isPresent()) {
			return false;
		}

		categoryRepository.save(Category.builder()
			.categoryName(parameter.getCategoryName())
			.sortValue(0)
			.build());

		return true;
	}

	@Override
	public boolean update(CategoryInput parameter) {
		Optional<Category> optionalCategory = categoryRepository.findById(parameter.getId());

		if (optionalCategory.isPresent()) {
			Category category = optionalCategory.get();
			category.setCategoryName(parameter.getCategoryName());
			category.setSortValue(parameter.getSortValue());
			categoryRepository.save(category);
		}

		return true;
	}

	@Override
	public boolean del(CategoryInput parameter) {

		List<Menu> menuList = menuRepository
			.findAllByCategoryId(parameter.getId());
		if (menuList != null) {
			for (Menu menu: menuList) {
				menuRepository.deleteById(menu.getId());
			}
		}

		categoryRepository.deleteById(parameter.getId());

		return true;
	}
}
