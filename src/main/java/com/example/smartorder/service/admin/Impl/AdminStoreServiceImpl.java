package com.example.smartorder.service.admin.Impl;


import static com.example.smartorder.common.error.ErrorCode.ALREADY_STORE_NAME_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.USER_NOT_CEO;

import com.example.smartorder.common.exception.AdminException;
import com.example.smartorder.dto.AdminStoreDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.AdminStore;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.admin.AdminStoreService;
import com.example.smartorder.type.UserRole;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminStoreServiceImpl implements AdminStoreService {

	private final StoreRepository storeRepository;
	private final MemberRepository memberRepository;

	@Override
	public Page<AdminStoreDto> list(Pageable pageable) {
		Page<Store> stores = storeRepository.findAll(pageable);

		return stores.map(AdminStoreDto::of);
	}

	@Override
	public void add(AdminStore.Add parameter) {

		Optional<Store> optionalStore = storeRepository.findByStoreName(parameter.getStoreName());
		if (optionalStore.isPresent()) {
			throw new AdminException(ALREADY_STORE_NAME_EXISTS);
		}

		Member member = memberRepository.findById(parameter.getUserId())
			.orElseThrow(() -> new AdminException(NOT_FOUND_USER));

		if (UserRole.ROLE_CEO != member.getUserRole()) {
			throw new AdminException(USER_NOT_CEO);
		}

		// TODO 입력 받은 주소 -> 위도 경도 불러와서 저장

		storeRepository.save(parameter.toEntity(member));
	}

	@Override
	public void update(Long storeId, AdminStore.Add parameter) {

		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new AdminException(NOT_FOUND_STORE));

		Optional<Store> optionalStore = storeRepository
			.existsByStoreNameExceptId(parameter.getStoreName(), storeId);

		if (optionalStore.isPresent()) {
			throw new AdminException(ALREADY_STORE_NAME_EXISTS);
		}

		Member member = memberRepository.findById(parameter.getUserId())
			.orElseThrow(() -> new AdminException(NOT_FOUND_USER));

		if (UserRole.ROLE_CEO != member.getUserRole()) {
			throw new AdminException(USER_NOT_CEO);
		}

		// TODO 입력 받은 주소 -> 위도 경도 불러와서 저장

		store.setMember(member);
		store.setStoreName(parameter.getStoreName());
		store.setZipcode(parameter.getZipcode());
		store.setAddr(parameter.getAddr());
		store.setAddrDetail(parameter.getAddrDetail());
		storeRepository.save(store);
	}

	@Override
	public void del(List<Long> idList) {
		for (Long id: idList) {
			Store store = storeRepository.findById(id)
				.orElseThrow(() -> new AdminException(NOT_FOUND_STORE));

			storeRepository.deleteById(id);

		}
	}

}
