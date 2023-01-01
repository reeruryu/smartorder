package com.example.smartorder.admin.service.Impl;

import com.example.smartorder.admin.dto.CategoryDto;
import com.example.smartorder.admin.model.CategoryInput;
import com.example.smartorder.category.entity.Category;
import com.example.smartorder.category.repository.CategoryRepository;
import com.example.smartorder.admin.service.AdminCategoryService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryDto> list() {
		List<Category> list = categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortValue"));

		return CategoryDto.of(list);
	}

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
		categoryRepository.deleteById(parameter.getId());

		return true;
	}
}
