package com.example.smartorder.service.Impl;

import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_CATEGORY;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STOREMENU;
import static com.example.smartorder.type.SaleState.ON_SALE;
import static com.example.smartorder.type.SaleState.SOLDOUT_FOR_ONE_DAY;

import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.common.exception.ValidationException;
import com.example.smartorder.dto.StoreMenuDto;
import com.example.smartorder.entity.Category;
import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.Store;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.mapper.StoreMenuMapper;
import com.example.smartorder.model.UpdateStoreMenu;
import com.example.smartorder.model.UpdateStoreOpenTime;
import com.example.smartorder.repository.CategoryRepository;
import com.example.smartorder.repository.MenuRepository;
import com.example.smartorder.repository.StoreMenuRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.StoreMenuService;
import com.example.smartorder.service.StoreService;
import com.example.smartorder.type.SaleState;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {
	private final StoreRepository storeRepository;

	@Override
	public void updateStoreOpenDay(Long storeId, String openDayList) {
		Store store = getOrElseThrow(storeId); // 가게 없을 시 등록 안됨 에러

		if (openDayList != null) {
			store.setOpenDay(openDayList);
			storeRepository.save(store);
		}
	}

	@Override
	public void updateStoreOpenTime(Long storeId, UpdateStoreOpenTime parameter) {
		Store store = getOrElseThrow(storeId); // 가게 없을 시 등록 안됨 에러

		LocalTime startTime = LocalTime.of(parameter.getStartHH(), parameter.getStartmm());
		LocalTime endTime = LocalTime.of(parameter.getEndHH(), parameter.getEndmm());

		if (startTime.isAfter(endTime)) {
			throw new ValidationException(END_FASTER_THAN_START);
		}

		store.setStartTime(startTime);
		store.setEndTime(endTime);
		storeRepository.save(store);
	}

	@Override
	public void updateStoreOpenYn(Long storeId, boolean openYn) {
		Store store = getOrElseThrow(storeId); // 가게 없을 시 등록 안됨 에러

		store.setOpenYn(openYn);
		storeRepository.save(store);

	}

	private Store getOrElseThrow(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND_STORE));
	}
}
