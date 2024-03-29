package com.example.smartorder.service.admin.impl;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_MENU_NAME_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_MENU;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.MenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.model.AdminMenu.Add;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.service.admin.AdminMenuService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminMenuServiceImpl implements AdminMenuService {

	private final MenuRepository menuRepository;
	private final CategoryRepository categoryRepository;
	private final StoreMenuRepository storeMenuRepository;

	@Override
	public Page<MenuDto> list(Long categoryId, Pageable pageable) {
		Page<Menu> menuList;

		if (categoryId == null) {
			menuList = menuRepository.findAll(pageable);

		} else {
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));

			menuList = menuRepository.findAllByCategoryId(categoryId, pageable);
		}

		return menuList.map(MenuDto::of);
	}

	@Override
	public void add(Add parameter) {
		Category category = categoryRepository.findById(parameter.getCategoryId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));

		boolean result = menuRepository.existsByMenuName(parameter.getMenuName());
		if (result) {
			throw new CustomException(ALREADY_MENU_NAME_EXISTS);
		}

		menuRepository.save(parameter.toEntity(category));
	}

	@Override
	public void update(Long menuId, Add parameter) {
		Menu menu = menuRepository.findById(menuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_MENU));

		Category category = categoryRepository.findById(parameter.getCategoryId())
			.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));

		Optional<Menu> optionalMenu
			= menuRepository.existsByMenuNameExceptId(parameter.getMenuName(), menuId);
		if (optionalMenu.isPresent()) {
			throw new CustomException(ALREADY_MENU_NAME_EXISTS);
		}

		menu.setCategory(category);
		menu.setMenuName(parameter.getMenuName());
		menu.setMenuPrice(parameter.getMenuPrice());
		menu.setSortValue(parameter.getSortValue());
		menu.setDescription(parameter.getDescription());
		menuRepository.save(menu);
	}

	@Override
	public void del(List<Long> idList) {

		// Store에 등록된 Menu 지우고
		storeMenuRepository.deleteAllByMenuIdIn(idList);

		// Menu 최종 지우기
		menuRepository.deleteAllByMenuIdIn(idList);
	}
}
