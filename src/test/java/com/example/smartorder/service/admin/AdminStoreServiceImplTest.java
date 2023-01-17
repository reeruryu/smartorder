package com.example.smartorder.service.admin;

import static com.example.smartorder.common.error.ErrorCode.ALREADY_STORE_NAME_EXISTS;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_STORE;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.USER_NOT_CEO;
import static com.example.smartorder.type.UserRole.ROLE_CEO;
import static com.example.smartorder.type.UserRole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.AdminStoreDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.AdminStore;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.StoreRepository;
import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class AdminStoreServiceImplTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private AdminStoreServiceImpl storeService;

	/**
	 * 매장 리스트 보기
	 */
	@Test
	@DisplayName("매장 리스트 보기")
	void list() {
		// given
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_CEO)
			.build();
		List<Store> storeList = Arrays.asList(
			Store.builder()
				.id(1L).member(member).build(),
			Store.builder()
				.id(2L).member(member).build()
		);
		Page<Store> stores = new PageImpl<>(storeList);

		given(storeRepository.findAll(any(Pageable.class)))
			.willReturn(stores);

		// when
		Page<AdminStoreDto> storeDtos = storeService.list(Pageable.unpaged());

		// then
		assertEquals(1L, storeDtos.getContent().get(0).getId());
		assertEquals(2L, storeDtos.getContent().get(1).getId());
		assertEquals(2, storeDtos.getTotalElements());
		assertEquals(1, storeDtos.getTotalPages());
		assertEquals(0, storeDtos.getNumber());
	}

	/**
	 * 매장 추가
	 */
	@Test
	@DisplayName("매장 추가 성공")
	void addSuccess() {
		// given
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_CEO)
			.build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실점")
			.build();

		given(storeRepository.findByStoreName(anyString()))
			.willReturn(Optional.empty());
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

		// when
		storeService.add(req);

		// then
		verify(storeRepository, times(1)).save(captor.capture());
		assertEquals("ceo@naver.com", captor.getValue().getMember().getUserId());
		assertEquals(ROLE_CEO, captor.getValue().getMember().getUserRole());
		assertEquals("잠실점", captor.getValue().getStoreName());
	}

	@Test
	@DisplayName("매장 추가 실패 - 중복된 가게명")
	void addFail_alreadyStoreNameExists() {
		// given
		Store store = Store.builder()
			.storeName("잠실점").build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실점")
			.build();

		given(storeRepository.findByStoreName(anyString()))
			.willReturn(Optional.of(store));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.add(req));

		// then
		assertEquals(ALREADY_STORE_NAME_EXISTS, exception.getErrorCode());
	}

	@Test
	@DisplayName("매장 추가 실패 - 해당 유저 없음")
	void addFail_notFoundUser() {
		// given
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실점")
			.build();

		given(storeRepository.findByStoreName(anyString()))
			.willReturn(Optional.empty());
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.add(req));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("매장 추가 실패 - 해당 유저 ceo 권한이 아님")
	void addFail_userNotCeo() {
		// given
		Member member = Member.builder()
			.userId("user@naver.com")
			.userRole(ROLE_USER)
			.build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실점")
			.build();

		given(storeRepository.findByStoreName(anyString()))
			.willReturn(Optional.empty());
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.add(req));

		// then
		assertEquals(USER_NOT_CEO, exception.getErrorCode());
	}

	/**
	 * 매장 정보 수정
	 */
	@Test
	@DisplayName("매장 정보 수정 성공")
	void updateSuccess() {
		// given
		Store store = Store.builder()
			.storeName("잠실점").build();
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_CEO)
			.build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실역점")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeRepository.existsByStoreNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.empty());
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

		// when
		storeService.update(1L, req);

		// then
		verify(storeRepository, times(1)).save(captor.capture());
		assertEquals("ceo@naver.com", captor.getValue().getMember().getUserId());
		assertEquals("잠실역점", captor.getValue().getStoreName());
	}

	@Test
	@DisplayName("매장 정보 수정 실패 - 해당 가게가 없음")
	void updateFail_notFoundStore() {
		// given
		AdminStore.Add req = AdminStore.Add.builder()
			.storeName("잠실역점")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.update(1L, req));

		// then
		assertEquals(NOT_FOUND_STORE, exception.getErrorCode());
	}

	@Test
	@DisplayName("매장 정보 수정 실패 - 중복된 가게명")
	void updateFail_alreadyStoreNameExists() {
		// given
		Store store = Store.builder()
			.storeName("잠실점").build();
		Store store2 = Store.builder()
			.storeName("잠실역점").build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실역점")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeRepository.existsByStoreNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.of(store2));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.update(1L, req));

		// then
		assertEquals(ALREADY_STORE_NAME_EXISTS, exception.getErrorCode());
	}

	@Test
	@DisplayName("매장 정보 수정 실패 - 해당 유저 없음")
	void updateFail_notFoundUser() {
		// given
		Store store = Store.builder()
			.storeName("잠실점").build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실역점")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeRepository.existsByStoreNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.empty());
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.update(1L, req));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("매장 정보 수정 실패 - 점주 권한이 없는 유저")
	void updateFail_userNotCeo() {
		// given
		Store store = Store.builder()
			.storeName("잠실점").build();
		Member member = Member.builder()
			.userId("ceo@naver.com")
			.userRole(ROLE_USER)
			.build();
		AdminStore.Add req = AdminStore.Add.builder()
			.userId("h2ju1004@gmail.com")
			.storeName("잠실역점")
			.build();

		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(storeRepository.existsByStoreNameExceptId(anyString(), anyLong()))
			.willReturn(Optional.empty());
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> storeService.update(1L, req));

		// then
		assertEquals(USER_NOT_CEO, exception.getErrorCode());
	}

	/**
	 * 매장 삭제
	 */
	@Test
	@DisplayName("매장 삭제 성공")
	void delSuccess() {
		// given
		List<Long> req = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

		ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

		// when
		storeService.del(req);

		// then
		verify(storeRepository, times(1))
			.deleteAllByStoreIdIn(captor.capture());

	}

}