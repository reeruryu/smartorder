package com.example.smartorder.service.smartorder.impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.SaleState.SOLDOUT_FOR_ONE_DAY;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.FrontStoreMenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.StoreMenuService;
import com.example.smartorder.type.SaleState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreMenuServiceImpl implements StoreMenuService {

	private final MemberRepository memberRepository;
	private final StoreMenuRepository storeMenuRepository;
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public List<StoreMenuDto> list(Long storeId, Long categoryId, String userId) {

		Store store = validateStoreOwner(storeId, userId);

		List<StoreMenu> storeMenuList;
		if (categoryId == null) { // 스토어 메뉴 전체 보기
			storeMenuList = storeMenuRepository.findAllByStore(store);

		} else { // 해당 카테고리 스토어 메뉴 보기
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));

			storeMenuList = storeMenuRepository.findAllByCategoryIdAndStoreId(categoryId, storeId);
		}

		return storeMenuList.stream().map(StoreMenuDto::of)
			.collect(Collectors.toList());

	}

	@Override
	public void updateHidden(Long storeId, Long storeMenuId,
		boolean hidden, String userId) {

		validateStoreOwner(storeId, userId);

		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STOREMENU));

		storeMenu.setHidden(hidden);

		storeMenuRepository.save(storeMenu);

	}

	@Override
	public void updateSaleState(Long storeId, Long storeMenuId,
		SaleState saleState, String userId) {

		validateStoreOwner(storeId, userId);

		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STOREMENU));

		storeMenu.setSaleState(saleState);
		if (saleState == SOLDOUT_FOR_ONE_DAY) {
			storeMenu.setSoldOutDt(LocalDateTime.now());
		}

		storeMenuRepository.save(storeMenu);

	}

	@Override
	public Page<FrontStoreMenuDto> front(Long storeId, Long categoryId,
		Pageable pageable, String userId) {

		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		Page<StoreMenu> storeMenus;
		if (categoryId == null) {
			storeMenus = storeMenuRepository.findByStoreIdExceptHiddenTrue(storeId, pageable);
		} else {
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));

			storeMenus = storeMenuRepository.findByCategoryIdAndStoreIdExceptHiddenTrue(
				categoryId, storeId, pageable);
		}

		return storeMenus.map(FrontStoreMenuDto::of);
	}

	private Store validateStoreOwner(Long storeId, String userId) {
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		if (!Objects.equals(userId, store.getMember().getUserId())) {
			throw new CustomException(CANNOT_ACCESS_STORE);
		}

		return store;
	}
}
