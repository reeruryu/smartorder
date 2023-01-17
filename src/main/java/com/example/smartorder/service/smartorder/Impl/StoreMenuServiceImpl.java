package com.example.smartorder.service.smartorder.Impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.SaleState.ON_SALE;
import static com.example.smartorder.type.SaleState.SOLDOUT_FOR_ONE_DAY;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.FrontStoreMenuDto;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.mapper.StoreMenuMapper;
import com.example.smartorder.model.StoreMenuParam;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.StoreMenuService;
import com.example.smartorder.type.SaleState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class StoreMenuServiceImpl implements StoreMenuService {

	private final MemberRepository memberRepository;
	private final StoreMenuRepository storeMenuRepository;
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final StoreMenuMapper storeMenuMapper;


	@Override
	public List<StoreMenuDto> listStoreMenu(Long storeId, Long categoryId, String userId) {
		// user의 store가 아니면 throw new NOT_USER_STORE
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE)); // 가게 없을 시 등록 안됨 에러

		if (!Objects.equals(userId, store.getMember().getUserId())) {
			throw new CustomException(CANNOT_ACCESS_STORE);
		}

		if (categoryId != null) {
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY)); // 카테고리 없을 시
		}

		return storeMenuMapper.selectList(storeId, categoryId);

	}

	@Override
	public void updateHiddenYn(Long storeId, Long storeMenuId,
		boolean hidden, String userId) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STOREMENU));

		storeMenu.setHiddenYn(hidden);

		storeMenuRepository.save(storeMenu);

	}

	@Override
	public void updateSaleState(Long storeId, Long storeMenuId,
		SaleState saleState, String userId) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STOREMENU));

		storeMenu.setSaleState(saleState);
		if (saleState == SOLDOUT_FOR_ONE_DAY) {
			storeMenu.setSoldOutDt(LocalDateTime.now());
		}

		storeMenuRepository.save(storeMenu);

	}

	@Override
	public Page<FrontStoreMenuDto> frontStoreMenu(Long storeId, Long categoryId,
		Pageable pageable, String userId) {

		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));

		Page<StoreMenu> storeMenus;
		if (categoryId == null) {
			storeMenus = storeMenuRepository.findByStoreIdExceptHiddenYnTrue(storeId, pageable);
		} else {
			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(NOT_FOUND_CATEGORY));

			storeMenus = storeMenuRepository.findByCategoryIdAndStoreIdExceptHiddenYnTrue(
				categoryId, storeId, pageable);
		}

		return storeMenus.map(FrontStoreMenuDto::of);
	}
}
