package com.example.smartorder.service.pay;

import static com.example.smartorder.common.error.ErrorCode.USER_STATUS_STOP;
import static com.example.smartorder.type.UserStatus.STATUS_ING;
import static com.example.smartorder.type.UserStatus.STATUS_STOP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import com.example.smartorder.repository.ConvPayRepository;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.service.pay.Impl.ConvPayServiceImpl;
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
class ConvPayServiceImplTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private ConvPayRepository convPayRepository;

	@InjectMocks
	private ConvPayServiceImpl convPayService;

	@Test
	@DisplayName("간편 페이 충전 성공 - 처음 충전")
	void addMoneySuccess_convPayNull() {
		// given
		Member member = Member.builder()
			.id(1L).userStatus(STATUS_ING).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(convPayRepository.findByMember(any()))
			.willReturn(null);
		ArgumentCaptor<ConvPay> captor = ArgumentCaptor.forClass(ConvPay.class);

		// when
		convPayService.addMoney(10000, "user@naver.com");

		// then
		verify(convPayRepository, times(1)).save(captor.capture());
		assertEquals(10000, captor.getValue().getBalance());

	}

	@Test
	@DisplayName("간편 페이 충전 성공 - 충전한 적 있음")
	void addMoneySuccess_convPayNotNull() {
		// given
		Member member = Member.builder()
			.id(1L).userStatus(STATUS_ING).build();
		ConvPay convPay = ConvPay.builder()
			.balance(5000).member(member).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(convPayRepository.findByMember(any()))
			.willReturn(convPay);
		ArgumentCaptor<ConvPay> captor = ArgumentCaptor.forClass(ConvPay.class);

		// when
		convPayService.addMoney(10000, "user@naver.com");

		// then
		verify(convPayRepository, times(1)).save(captor.capture());
		assertEquals(15000, captor.getValue().getBalance());

	}

	@Test
	@DisplayName("간편 페이 충전 실패 - 정지된 회원")
	void addMoneyFail_userStatusStop() {
		// given
		Member member = Member.builder()
			.id(1L).userStatus(STATUS_STOP).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		ArgumentCaptor<ConvPay> captor = ArgumentCaptor.forClass(ConvPay.class);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> convPayService.addMoney(10000, "user@naver.com"));

		// then
		assertEquals(USER_STATUS_STOP, exception.getErrorCode());

	}
}