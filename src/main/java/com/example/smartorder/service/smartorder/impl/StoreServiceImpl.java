package com.example.smartorder.service.smartorder.impl;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.StoreParam;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.StoreService;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {
	private final StoreRepository storeRepository;
	private final MemberRepository memberRepository;

	@CacheEvict(value = "location", allEntries = true)
	@Override
	public void updateStoreOpenDay(Long storeId, String openDayList, String userId) {

		Store store = getOrElseThrow(storeId);

		checkStoreCeo(userId, store);

		if (openDayList != null) {
			store.setOpenDay(openDayList);
			storeRepository.save(store);
		}
	}

	@CacheEvict(value = "location", allEntries = true)
	@Override
	public void updateStoreOpenTime(Long storeId, StoreParam.OpenTime parameter, String userId) {
		Store store = getOrElseThrow(storeId);

		checkStoreCeo(userId, store);

		LocalTime startTime = LocalTime.of(parameter.getStartHH(), parameter.getStartmm());
		LocalTime endTime = LocalTime.of(parameter.getEndHH(), parameter.getEndmm());

		if (startTime.isAfter(endTime)) {
			throw new CustomException(END_FASTER_THAN_START);
		}

		store.setStartTime(startTime);
		store.setEndTime(endTime);
		storeRepository.save(store);
	}

	@CacheEvict(value = "location", allEntries = true)
	@Override
	public void updateStoreOpen(Long storeId, boolean open, String userId) {
		Store store = getOrElseThrow(storeId);

		checkStoreCeo(userId, store);

		store.setOpen(open);
		storeRepository.save(store);

	}

	private Store getOrElseThrow(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_STORE));
	}

	private void checkStoreCeo(String userId, Store store) {
		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		if (!member.equals(store.getMember())) {
			throw new CustomException(CANNOT_ACCESS_STORE);
		}
	}
}
