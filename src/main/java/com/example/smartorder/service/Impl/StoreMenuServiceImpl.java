package com.example.smartorder.service.Impl;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;

import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.mapper.StoreMenuMapper;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.StoreMenuService;
import com.example.smartorder.type.SaleState;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class StoreMenuServiceImpl implements StoreMenuService {
	private final StoreMenuRepository storeMenuRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	private final CategoryRepository categoryRepository;
	private final StoreMenuMapper storeMenuMapper;

	public boolean createAllStoreMenu(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE)); // 가게 없을 시 등록 안됨 에러

		List<Menu> menuList = menuRepository.findAll();
		if (!CollectionUtils.isEmpty(menuList)) {
			for (Menu menu: menuList) {
				storeMenuRepository.save(StoreMenu.builder()
					.menu(menu)
					.store(store)
					.saleState(SaleState.ON_SALE)
					.hiddenYn(false)
					.build());
			}
		}

		return true;
	}

	@Override
	public boolean createNewStoreMenu(List<Long> menuIdList) {
		List<StoreMenu> storeMenuList = new ArrayList<>();
		List<Menu> menuList = menuRepository.findByIdIn(menuIdList);
		List<Store> storeList = storeRepository.findAll();

		if (!CollectionUtils.isEmpty(storeList) && !CollectionUtils.isEmpty(menuList)) {
			for (Store store: storeList) {
				for (Menu menu: menuList) {
					storeMenuList.add(StoreMenu.builder()
						.menu(menu)
						.store(store)
						.saleState(SaleState.ON_SALE)
						.hiddenYn(false)
						.build());
				}
			}
		}
		storeMenuRepository.saveAll(storeMenuList);

		return true;

	}

	@Override
	public List<StoreMenuDto> listStoreMenu(Long storeId, Long categoryId) { // , String userId)
		// user의 store가 아니면 thorw new NOT_USER_STORE

		Store Store = storeRepository.findById(storeId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE)); // 가게 없을 시 등록 안됨 에러

		if (categoryId != null) {
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY)); // 카테고리 없을 시
		}

		return storeMenuMapper.selectList(storeId, categoryId);

	}
}
