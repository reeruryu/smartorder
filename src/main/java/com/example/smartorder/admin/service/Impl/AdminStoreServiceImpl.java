package com.example.smartorder.admin.service.Impl;

import static com.example.smartorder.member.type.UserRole.ROLE_CEO;

import com.example.smartorder.admin.dto.StoreDto;
import com.example.smartorder.admin.mapper.StoreMapper;
import com.example.smartorder.admin.model.StoreInput;
import com.example.smartorder.admin.model.StoreParam;
import com.example.smartorder.admin.service.AdminStoreService;
import com.example.smartorder.member.entity.Member;
import com.example.smartorder.member.repository.MemberRepository;
import com.example.smartorder.entity.Store;
import com.example.smartorder.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class AdminStoreServiceImpl implements AdminStoreService {

	private final StoreRepository storeRepository;
	private final MemberRepository memberRepository;
	private final StoreMapper storeMapper;

	@Override
	public List<StoreDto> list(StoreParam parameter) {
		long totalCount = storeMapper.selectListCount(parameter);

		List<StoreDto> list = storeMapper.selectList(parameter);

		if (!CollectionUtils.isEmpty(list)) {
			int i = 0;
			for (StoreDto x: list) {
				x.setTotalCount(totalCount);
				x.setSeq(totalCount - parameter.getPageStart() - i);
				i++;
			}
		}

		return list;
	}

	@Override
	public StoreDto getById(long id) {
		return storeRepository.findById(id).map(StoreDto::of).orElse(null);
	}

	@Override
	public boolean add(StoreInput parameter) {

		if (getByStoreName(parameter).isPresent()) {
			return false;
		}

		Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
		if (!optionalMember.isPresent()) {
			return false;
		}
		Member member = optionalMember.get();
		if (ROLE_CEO != member.getUserRole()) {
			return false;
		}

		Store store = Store.builder()
			.member(member)
			.storeName(parameter.getStoreName())
			.zipcode(parameter.getZipcode())
			.addr(parameter.getAddr())
			.addrDetail(parameter.getAddrDetail()) // 주소 입력 시 위도 경도 프론트에서 받아 옴
			.build();
		storeRepository.save(store);

		return true;
	}

	@Override
	public boolean set(StoreInput parameter) {

		Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
		if (!optionalMember.isPresent()) {
			return false;
		}
		Member member = optionalMember.get();
		if (ROLE_CEO != member.getUserRole()) {
			return false;
		}

		Optional<Store> optionalStore = storeRepository.findById(parameter.getId());
		Store store = optionalStore.get();

		store.setMember(member);
		store.setStoreName(parameter.getStoreName());
		store.setZipcode(parameter.getZipcode());
		store.setAddr(parameter.getAddr());
		store.setAddrDetail(parameter.getAddrDetail()); // 주소 입력 시 위도 경도 프론트에서 받아 옴
		storeRepository.save(store);

		return true;
	}

	private Optional<Store> getByStoreName(StoreInput parameter) {
		return storeRepository.findByStoreName(parameter.getStoreName());
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
					storeRepository.deleteById(id);
				}
			}
		}

		return true;
	}

}
