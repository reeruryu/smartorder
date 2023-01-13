package com.example.smartorder.service.Impl;


import com.example.smartorder.admin.mapper.StoreMapper;
import com.example.smartorder.dto.StoreDto;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

	private final StoreRepository storeRepository;
	private final StoreMapper storeMapper;

//	@Cacheable()
	@Override
	public List<StoreDto> getNearStoreList(String userId, double lat, double lnt) {

		List<StoreDto> storeDtoList
			= storeMapper.selectNearList(lat, lnt);

		return storeDtoList;
	}
}
