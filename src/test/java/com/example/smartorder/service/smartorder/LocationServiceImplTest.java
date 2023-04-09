package com.example.smartorder.service.smartorder;

import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_LOCATION;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.LocationDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.mapper.LocationMapper;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.smartorder.impl.LocationServiceImpl;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

	@Mock
	private LocationMapper locationMapper;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private LocationServiceImpl locationService;

	/**
	 * 사용자(고객)의 위치를 저장합니다.
	 */
	@Test
	@DisplayName("고객 위치 정보 update 성공")
	void updateLocationSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

		// when
		locationService.updateLocation(128.124, 37.809, "user@gmail.com");

		// then
		verify(memberRepository, times(1)).save(captor.capture());
		assertEquals(128.124, captor.getValue().getLat());
		assertEquals(37.809, captor.getValue().getLnt());

	}

	@Test
	@DisplayName("고객 위치 정보 update 실패 - 해당 유저 없음")
	void updateLocationFail_notFoundUser() {
		// given
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> locationService.updateLocation(128.124, 37.809, "user@gmail.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());

	}

	/**
	 * 현재 위치 2km 반경 내 가까운 가게 10곳을 조회합니다.
	 */
	@Test
	@DisplayName("가까운 가게 조회 성공")
	void getNearStoreListSuccess() {
		// given
		List<LocationDto> locationList = Arrays.asList(
			LocationDto.builder()
				.id(1L).storeName("송파점")
				.zipcode("05507")
				.addr("서울 송파구 올림픽로 435")
				.addrDetail("1층")
				.startTime(LocalTime.of(9,0,0))
				.endTime(LocalTime.of(22,0,0))
				.openDay("0,1,2,3,4")
				.open(true)
				.distance(0.34)
				.build(),
			LocationDto.builder()
				.id(2L).storeName("잠실역점")
				.zipcode("05508")
				.addr("서울 송파구 올림픽로 500")
				.addrDetail("3층")
				.startTime(LocalTime.of(9,0,0))
				.endTime(LocalTime.of(22,0,0))
				.openDay("0,1,2,3,4")
				.open(true)
				.distance(0.54)
				.build()
		);
		Member member = Member.builder()
			.id(1L).lat(127.1034).lnt(38.094).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(locationMapper.selectNearList(anyDouble(), anyDouble()))
			.willReturn(locationList);

		// when
		List<LocationDto> list =
			locationService.getNearStoreList("user@gmail.com");

		// then
		assertEquals(2, list.size());
	}

	@Test
	@DisplayName("가까운 가게 조회 실패 - 해당 유저 없음")
	void getNearStoreListFail_notFoundUser() {
		// given
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> locationService.getNearStoreList("user@gmail.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("가까운 가게 조회 실패 - 위치 정보가 등록되지 않음")
	void getNearStoreListFail_notFoundLocation() {
		// given
		Member member = Member.builder()
			.id(1L).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> locationService.getNearStoreList("user@gmail.com"));

		// then
		assertEquals(NOT_FOUND_LOCATION, exception.getErrorCode());
	}
}