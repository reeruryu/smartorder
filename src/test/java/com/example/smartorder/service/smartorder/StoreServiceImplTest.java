package com.example.smartorder.service.smartorder;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.type.UserRole.ROLE_CEO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.StoreParam;
import com.example.smartorder.model.StoreParam.OpenTime;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.Impl.StoreServiceImpl;
import java.time.LocalTime;
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
public class StoreServiceImplTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private StoreServiceImpl storeService;


	/**
	 * 오픈 요일을 변경합니다.
	 */
	@Test
	@DisplayName("오픈 요일 변경 성공")
	void updateStoreOpenDaySuccess() {
		// given
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_CEO)
			.build();
		Store store = Store.builder()
			.id(1L).member(member)
			.openDay("0,1,2,3")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

		// when
		storeService.updateStoreOpenDay(1L, "2,4,6", "ceo@naver.com");

		// then
		verify(storeRepository, times(1)).save(captor.capture());
		assertEquals("2,4,6", captor.getValue().getOpenDay());

	}

	@Test
	@DisplayName("오픈 요일 변경 실패 - 존재하지 않는 가게")
	void updateStoreOpenDayFail_notFoundStore() {
		// given
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpenDay(
				1L, "2,4,6", "ceo@naver.com"));

		// then
		assertEquals(NOT_FOUND_STORE, exception.getErrorCode());

	}

	@Test
	@DisplayName("오픈 요일 변경 실패 - 존재하지 않는 회원")
	void updateStoreOpenDayFail_notFoundUser() {
		// given
		Store store = Store.builder()
			.id(1L).build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpenDay(
				1L, "2,4,6", "ceo@naver.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("오픈 요일 변경 실패 - 매장 점주와 입력한 회원 정보 불일치")
	void updateStoreOpenDayFail_cannotAccessStore() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Member member2 = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(member2)
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpenDay(
				1L, "2,4,6", "ceo@naver.com"));

		// then
		assertEquals(CANNOT_ACCESS_STORE, exception.getErrorCode());

	}

	/**
	 * 오픈 시간을 변경합니다.
	 */
	@Test
	@DisplayName("오픈 시간 변경 성공")
	void updateStoreOpenTimeSuccess() {
		// given
		StoreParam.OpenTime req =
			new OpenTime(9, 0, 21, 30);
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_CEO)
			.build();
		Store store = Store.builder()
			.id(1L).member(member)
			.openDay("0,1,2,3")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

		// when
		storeService.updateStoreOpenTime(1L, req, "ceo@gmail.com");

		// then
		verify(storeRepository, times(1)).save(captor.capture());
		assertEquals(LocalTime.of(9, 0), captor.getValue().getStartTime());
		assertEquals(LocalTime.of(21, 30), captor.getValue().getEndTime());

	}

	@Test
	@DisplayName("오픈 시간 변경 실패 - 존재하지 않는 가게")
	void updateStoreOpenTimeFail_notFoundStore() {
		// given
		StoreParam.OpenTime req =
			new OpenTime(9, 0, 21, 30);

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpenTime(1L, req, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_STORE, exception.getErrorCode());

	}

	@Test
	@DisplayName("오픈 시간 변경 실패 - 존재하지 않는 회원")
	void updateStoreOpenTimeFail_notFoundUser() {
		StoreParam.OpenTime req =
			new OpenTime(9, 0, 21, 30);
		Store store = Store.builder()
			.id(1L).build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpenTime(1L, req, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("오픈 시간 변경 실패 - 매장 점주와 입력한 회원 정보 불일치")
	void updateStoreOpenTimeFail_cannotAccessStore() {
		// given
		StoreParam.OpenTime req =
			new OpenTime(9, 0, 21, 30);
		Member member = Member.builder()
			.id(1L).build();
		Member member2 = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(member2)
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpenTime(1L, req, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_ACCESS_STORE, exception.getErrorCode());

	}

	/**
	 * 수동으로 오픈 시간을 변경합니다. (ex) 영업임시중단)
	 */
	@Test
	@DisplayName("수동 오픈 변경 성공")
	void updateStoreOpenSuccess() {
		// given
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_CEO)
			.build();
		Store store = Store.builder()
			.id(1L).member(member)
			.open(true)
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

		// when
		storeService.updateStoreOpen(1L, false, "ceo@gmail.com");

		// then
		verify(storeRepository, times(1)).save(captor.capture());
		assertEquals(false, captor.getValue().isOpen());

	}

	@Test
	@DisplayName("수동 오픈 변경 실패 - 존재하지 않는 가게")
	void updateStoreOpen_notFoundStore() {
		// given
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpen(1L, false, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_STORE, exception.getErrorCode());

	}

	@Test
	@DisplayName("수동 오픈 변경 실패 - 존재하지 않는 회원")
	void updateStoreOpenFail_notFoundUser() {
		Store store = Store.builder()
			.id(1L).build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpen(1L, false, "ceo@gmail.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("수동 오픈 변경 실패 - 매장 점주와 입력한 회원 정보 불일치")
	void updateStoreOpenFail_cannotAccessStore() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Member member2 = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(member2)
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.updateStoreOpen(1L, false, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_ACCESS_STORE, exception.getErrorCode());

	}
	
}
