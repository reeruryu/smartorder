package com.example.smartorder.admin.service.Impl;

import com.example.smartorder.admin.dto.MenuDto;
import com.example.smartorder.admin.mapper.MenuMapper;
import com.example.smartorder.admin.model.MenuInput;
import com.example.smartorder.admin.model.MenuParam;
import com.example.smartorder.admin.service.AdminMenuService;
import com.example.smartorder.entity.Category;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class AdminMenuServiceImpl implements AdminMenuService {

	private final MenuRepository menuRepository;
	private final CategoryRepository categoryRepository;

	private final StoreMenuRepository storeMenuRepository;
	private final MenuMapper menuMapper;

	@Override
	public List<MenuDto> list(MenuParam parameter) {
		long totalCount = menuMapper.selectListCount(parameter);

		List<MenuDto> list = menuMapper.selectList(parameter);

		if (!CollectionUtils.isEmpty(list)) {
			int i = 0;
			for (MenuDto x: list) {
				x.setTotalCount(totalCount);
				x.setSeq(totalCount - parameter.getPageStart() - i);
				i++;
			}
		}

		return list;
	}

	@Override
	public MenuDto getById(long id) {
		return menuRepository.findById(id).map(MenuDto::of).orElse(null);
	}

	@Override
	public boolean add(MenuInput parameter) {

		if (getByMenuName(parameter).isPresent()) {
			return false;
		}

		Optional<Category> optionalCategory = categoryRepository.findById(parameter.getCategoryId());
		if (!optionalCategory.isPresent()) {
			return false;
		}

		Menu menu = Menu.builder()
			.category(optionalCategory.get())
			.menuName(parameter.getMenuName())
			.menuPrice(parameter.getMenuPrice())
			.description(parameter.getDescription())
			.build();
		menuRepository.save(menu);

		return true;
	}

	@Override
	public boolean set(MenuInput parameter) {

		if (getByMenuName(parameter).isPresent()) {
			return false;
		}

		Optional<Menu> optionalMenu = menuRepository.findById(parameter.getId());
		if (!optionalMenu.isPresent()) {
			return false;
		}
		Menu menu = optionalMenu.get();

		Optional<Category> optionalCategory = categoryRepository.findById(parameter.getCategoryId());
		if (!optionalCategory.isPresent()) {
			return false;
		}

		menu.setCategory(optionalCategory.get());
		menu.setMenuName(parameter.getMenuName());
		menu.setMenuPrice(parameter.getMenuPrice());
		menu.setDescription(parameter.getDescription());

		menuRepository.save(menu);

		return true;
	}

	private Optional<Menu> getByMenuName(MenuInput parameter) {
		return menuRepository.findByMenuName(parameter.getMenuName());
	}

	@Override
	public boolean del(String idList) {

		if (idList != null && idList.length() > 0) {
			String[] ids = idList.split(",");
			for (String x: ids) {
				long id = 0L;
				try {
					id = Long.parseLong(x);
				} catch (Exception e) {
				}

				if (id > 0) {
					menuRepository.deleteById(id);
					// storemenu도 삭제해야 함
				}
			}
		}

		return true;
	}

}
