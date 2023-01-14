package com.example.smartorder.service.Impl;

import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;

import com.example.smartorder.common.exception.NotFoundException;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.UpdateStoreOpenTime;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.StoreService;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
