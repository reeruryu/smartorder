package com.example.smartorder.service.admin.Impl;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.type.SaleState.ON_SALE;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.admin.AdminStoreMenuService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class AdminStoreMenuServiceImpl implements AdminStoreMenuService {
	private final StoreMenuRepository storeMenuRepository;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	public void addAllStoreMenu(Long storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		List<Menu> menuList = menuRepository.findAll();
		if (!CollectionUtils.isEmpty(menuList)) {
			for (Menu menu: menuList) {
				storeMenuRepository.save(StoreMenu.builder()
					.menu(menu)
					.store(store)
					.saleState(ON_SALE)
					.hidden(false)
					.build());
			}
		}
	}

	@Override
	public void addNewStoreMenu(List<Long> menuIdList) {
		List<StoreMenu> storeMenuList = new ArrayList<>();
		List<Menu> menuList = menuRepository.findByIdIn(menuIdList);
		List<Store> storeList = storeRepository.findAll();

		if (!CollectionUtils.isEmpty(storeList) && !CollectionUtils.isEmpty(menuList)) {
			for (Store store: storeList) {
				for (Menu menu: menuList) {
					storeMenuList.add(StoreMenu.builder()
						.menu(menu)
						.store(store)
						.saleState(ON_SALE)
						.hidden(false)
						.build());
				}
			}
		}
		storeMenuRepository.saveAll(storeMenuList);

	}

}
