package com.example.smartorder.service.smartorder.Impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.SaleState.ON_SALE;
import static com.example.smartorder.type.SaleState.SOLDOUT_FOR_ONE_DAY;

import com.example.smartorder.common.exception.CustomException;
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
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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
	public void updateStoreMenu(Long storeId, Long storeMenuId,
		StoreMenuParam.Update parameter, String userId) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE)); // 가게 없을 시 등록 안됨 에러

		StoreMenu storeMenu = storeMenuRepository.findById(storeMenuId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STOREMENU)); // 가게 메뉴 없을 시 등록 안됨 에러

		SaleState saleState = parameter.getSaleState();
		storeMenu.setSaleState(saleState);
		storeMenu.setHiddenYn(parameter.isHiddenYn());

		storeMenuRepository.save(storeMenu);

	}

	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void updateSaleState() {

		List<StoreMenu> storeMenuList =
			storeMenuRepository.findAllBySaleState(SOLDOUT_FOR_ONE_DAY);

		if (!CollectionUtils.isEmpty(storeMenuList)) {
			for (StoreMenu storeMenu: storeMenuList) {
				storeMenu.setSaleState(ON_SALE);
			}
		}
		storeMenuRepository.saveAll(storeMenuList);

	}
}
