package com.example.smartorder.service.smartorder.impl;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_LOCATION;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.LocationDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.mapper.LocationMapper;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.smartorder.LocationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {
	private final LocationMapper locationMapper;
	private final MemberRepository memberRepository;

	@CacheEvict(value = "location", allEntries = true)
	@Override
	public void updateLocation(Double lat, Double lnt, String userId) {

		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		member.setLat(lat);
		member.setLnt(lnt);
		memberRepository.save(member);
	}

	@Cacheable(key = "#userId", value = "location")
	@Override
	public List<LocationDto> getNearStoreList(String userId) {

		Member member = memberRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		Double lat = member.getLat();
		Double lnt = member.getLnt();
		if (lat == null || lnt == null) {
			throw new CustomException(NOT_FOUND_LOCATION);
		}

		return locationMapper.selectNearList(lat, lnt);

	}

}
