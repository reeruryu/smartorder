package com.example.smartorder.service.smartorder;

import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_ACCESS_STORE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CANCEL_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_CANCELED_ORDER;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_COMPLETE_ORDER_STATE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_PREVIOUS_ORDER_STATE;
import static com.example.smartorder.common.error.ErrorCode.CANNOT_CHANGE_SAME_ORDER_STATE;
import static com.example.smartorder.common.error.ErrorCode.END_FASTER_THAN_START;
import static com.example.smartorder.common.error.ErrorCode.NOT_FOUND_USER;
import static com.example.smartorder.common.error.ErrorCode.NOT_TODAY_ORDER;
import static com.example.smartorder.common.error.ErrorCode.ORDER_ALREADY_CANCEL;
import static com.example.smartorder.type.OrderState.BEFORE_COOKING;
import static com.example.smartorder.type.OrderState.COOKING;
import static com.example.smartorder.type.OrderState.PICKUP_COMPLETE;
import static com.example.smartorder.type.OrderState.PICKUP_REQ;
import static com.example.smartorder.type.PayState.BEFORE_PAY;
import static com.example.smartorder.type.PayState.PAY_CANCEL;
import static com.example.smartorder.type.PayState.PAY_COMPLETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smartorder.common.exception.CustomException;
import com.example.smartorder.dto.OrderDto;
import com.example.smartorder.dto.OrderHistDto;
import com.example.smartorder.dto.OrderMenuDto;
import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import com.example.smartorder.model.OrderParam;
import com.example.smartorder.model.OrderParam.CeoUpdate;
import com.example.smartorder.repository.MemberRepository;
import com.example.smartorder.repository.OrderRepository;
import com.example.smartorder.repository.StoreRepository;
import com.example.smartorder.service.smartorder.Impl.OrderServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class OrderServiceImplTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private OrderServiceImpl orderService;

	/**
	 * 주문을 저장합니다.
	 */
	@Test
	@DisplayName("주문 저장 성공")
	void order() {
		// given
		OrderDto req = OrderDto.builder()
			.member(Member.builder().id(1L).build())
			.store(Store.builder().id(1L).build())
			.orderMenu(Arrays.asList(
				OrderMenuDto.builder()
					.menuName("아메리카노")
					.menuPrice(4500)
					.menuCount(2).build(),
				OrderMenuDto.builder()
					.menuName("라떼")
					.menuPrice(5100)
					.menuCount(3).build()
			))
			.orderPrice(10000).build();
		ArgumentCaptor<Orders> captor = ArgumentCaptor.forClass(Orders.class);

		// when
		Long orderId = orderService.order(req);

		// then
		verify(orderRepository, times(1)).save(captor.capture());
		assertEquals("아메리카노", captor.getValue().getOrderMenu().get(0).getMenuName());
		assertEquals("라떼", captor.getValue().getOrderMenu().get(1).getMenuName());
		assertEquals(2, captor.getValue().getOrderMenu().size());
		assertEquals(BEFORE_PAY, captor.getValue().getPayState());

	}

	/**
	 * 고객의 주문 내역을 불러옵니다.
	 */
	@Test
	@DisplayName("주문 내역을 불러오기 성공")
	void getOrderHistSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		List<Orders> orderList = Arrays.asList(
			Orders.builder()
				.id(123L).store(Store.builder().id(1L).build())
				.orderMenu(Arrays.asList(
					OrderMenuDto.builder().menuName("쿠키").build()))
				.orderPrice(10000).payState(PAY_COMPLETE)
				.orderState(PICKUP_COMPLETE).build(),
			Orders.builder()
				.id(246L).store(Store.builder().id(2L).build())
				.orderMenu(Arrays.asList(
					OrderMenuDto.builder().menuName("자몽에이드").build()))
				.orderPrice(20000).payState(PAY_CANCEL)
				.orderState(BEFORE_COOKING).build()
		);
		orderList.get(0).setRegDt(LocalDateTime.now());
		orderList.get(1).setRegDt(LocalDateTime.now());
		Page<Orders> orderHist = new PageImpl<>(orderList);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(orderRepository.findByMemberAndRegDtBetween(any(), any(), any(), any()))
			.willReturn(orderHist);

		// when
		Page<OrderHistDto> orderHistDtos =
			orderService.getOrderHist(Pageable.unpaged(),
				LocalDate.MIN, LocalDate.MAX, "user@naver.com");

		// then
		assertEquals(123L, orderHistDtos.getContent().get(0).getOrderId());
		assertEquals(246L, orderHistDtos.getContent().get(1).getOrderId());
		assertEquals(2, orderHistDtos.getTotalElements());
	}

	@Test
	@DisplayName("주문 내역을 불러오기 실패 - 해당 유저 없음")
	void getOrderHistFail_notFoundUser() {
		// given
		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.getOrderHist(Pageable.unpaged(),
				LocalDate.MIN, LocalDate.MAX, "user@naver.com"));

		// then
		assertEquals(NOT_FOUND_USER, exception.getErrorCode());
	}

	@Test
	@DisplayName("주문 내역을 불러오기 실패 - 종료 날짜가 시작 날짜보다 빠름")
	void getOrderHistFail_endFasterThanStart() {
		// given
		Member member = Member.builder()
			.id(1L).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.getOrderHist(Pageable.unpaged(),
				LocalDate.MAX, LocalDate.MIN, "user@naver.com"));

		// then
		assertEquals(END_FASTER_THAN_START, exception.getErrorCode());
	}

	/**
	 * 고객이 주문 취소를 합니다.
	 */
	@Test
	@DisplayName("고객 주문 취소 성공")
	void orderCancelSuccess() {
		// given
		OrderParam.Cancel req = OrderParam.Cancel.builder()
			.orderId(1L)
			.orderCancelReason("고객 변심").build();
		Member member = Member.builder()
			.id(1L).build();
		Orders order = Orders.builder()
			.id(1L).member(member).orderCancel(false)
			.orderState(BEFORE_COOKING).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));
		ArgumentCaptor<Orders> captor = ArgumentCaptor.forClass(Orders.class);

		// when
		Long orderId = orderService.orderCancel(req, "user@gmail.com");

		// then
		verify(orderRepository, times(1)).save(captor.capture());
		assertTrue(captor.getValue().isOrderCancel());
		assertNotNull(captor.getValue().getCancelDt());
	}

	@Test
	@DisplayName("고객 주문 취소 실패 - 해당 유저의 주문이 아닙니다.")
	void orderCancelFail_cannotAccessOrder() {
		// given
		OrderParam.Cancel req = OrderParam.Cancel.builder()
			.orderId(1L)
			.orderCancelReason("고객 변심").build();
		Member member = Member.builder()
			.id(1L).build();
		Member member2 = Member.builder()
			.id(2L).build();
		Orders order = Orders.builder()
			.id(1L).member(member2).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.orderCancel(req, "user@gmail.com"));

		// then
		assertEquals(CANNOT_ACCESS_ORDER, exception.getErrorCode());
	}

	@Test
	@DisplayName("고객 주문 취소 실패 - 이미 취소된 주문입니다.")
	void orderCancelFail_orderAlreadyCancel() {
		// given
		OrderParam.Cancel req = OrderParam.Cancel.builder()
			.orderId(1L)
			.orderCancelReason("고객 변심").build();
		Member member = Member.builder()
			.id(1L).build();
		Orders order = Orders.builder()
			.id(1L).member(member).orderCancel(true)
			.orderState(BEFORE_COOKING).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.orderCancel(req, "user@gmail.com"));

		// then
		assertEquals(ORDER_ALREADY_CANCEL, exception.getErrorCode());
	}


	@Test
	@DisplayName("고객 주문 취소 실패 - 조리 전일 때만 취소가 가능합니다.")
	void orderCancelFail_cannotCancelOrder() {
		// given
		OrderParam.Cancel req = OrderParam.Cancel.builder()
			.orderId(1L)
			.orderCancelReason("고객 변심").build();
		Member member = Member.builder()
			.id(1L).build();
		Orders order = Orders.builder()
			.id(1L).member(member).orderCancel(false)
			.orderState(COOKING).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.orderCancel(req, "user@gmail.com"));

		// then
		assertEquals(CANNOT_CANCEL_ORDER, exception.getErrorCode());
	}

	/**
	 * 점주가 매장 주문 내역을 조회합니다.
	 */
	@Test
	@DisplayName("점주 매장 주문 내역 조회 성공")
	void getCeoOrderHistSuccess() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		List<Orders> orderList = Arrays.asList(
			Orders.builder()
				.id(123L).store(store)
				.orderMenu(Arrays.asList(
					OrderMenuDto.builder().menuName("쿠키").build()))
				.orderPrice(10000).payState(PAY_COMPLETE)
				.orderState(PICKUP_COMPLETE).build(),
			Orders.builder()
				.id(246L).store(store)
				.orderMenu(Arrays.asList(
					OrderMenuDto.builder().menuName("자몽에이드").build()))
				.orderPrice(20000).payState(PAY_CANCEL)
				.orderState(PICKUP_COMPLETE).build()
		);
		orderList.get(0).setRegDt(LocalDateTime.now());
		orderList.get(1).setRegDt(LocalDateTime.now());
		Page<Orders> orderHist = new PageImpl<>(orderList);

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findByStoreAndRegDtBetween(any(), any(), any(), any()))
			.willReturn(orderHist);

		// when
		Page<OrderHistDto> orderHistDtos =
			orderService.getCeoOrderHist(Pageable.unpaged(),
				LocalDate.MIN, LocalDate.MAX, 1L, "ceo@naver.com");

		// then
		assertEquals(123L, orderHistDtos.getContent().get(0).getOrderId());
		assertEquals(246L, orderHistDtos.getContent().get(1).getOrderId());
		assertEquals(2, orderHistDtos.getTotalElements());
	}

	@Test
	@DisplayName("점주 매장 주문 내역 조회 실패 - 점주의 매장이 아닙니다.")
	void getCeoOrderHistFail_cannotAccessStore() {
		// given
		Member member = Member.builder()
			.id(1L).build();
		Member member2 = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(member2).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.getCeoOrderHist(Pageable.unpaged(),
				LocalDate.MIN, LocalDate.MAX, 1L, "ceo@naver.com"));

		// then
		assertEquals(CANNOT_ACCESS_STORE, exception.getErrorCode());
	}

	/**
	 * 점주가 주문을 취소합니다.
	 */
	@Test
	@DisplayName("점주 주문 취소 성공")
	void orderCeoCancelSuccess() {
		// given
		OrderParam.CeoCancel req = OrderParam.CeoCancel.builder()
			.orderId(1L).storeId(1L)
			.orderCancelReason("재료 소진").build();
		Member member = Member.builder()
			.id(1L).build();
		Store store = Store.builder()
			.id(1L).member(member).build();
		Orders order = Orders.builder()
			.id(1L).store(store).orderCancel(false)
			.orderState(BEFORE_COOKING).build();

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(member));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));
		ArgumentCaptor<Orders> captor = ArgumentCaptor.forClass(Orders.class);

		// when
		Long orderId = orderService.orderCeoCancel(req, "ceo@gmail.com");

		// then
		verify(orderRepository, times(1)).save(captor.capture());
		assertEquals(PAY_CANCEL, captor.getValue().getPayState());
		assertTrue(captor.getValue().isOrderCancel());
		assertNotNull(captor.getValue().getCancelDt());
	}

	/**
	 * 점주가 현재(오늘)의 주문 상태를 변경합니다.
	 */
	@Test
	@DisplayName("주문 상태 변경 성공")
	void updateOrderStateSuccess() {
		// given
		CeoUpdate req = CeoUpdate.builder()
			.orderId(1L).storeId(1L)
			.orderState(COOKING).build();
		Member ceo = Member.builder()
			.id(1L).build();
		Member user = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(ceo).build();
		Orders order = Orders.builder()
			.id(1L).member(user).orderCancel(false)
			.orderState(BEFORE_COOKING).build();
		order.setRegDt(LocalDateTime.now());

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(ceo));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));
		ArgumentCaptor<Orders> captor = ArgumentCaptor.forClass(Orders.class);

		// when
		Long orderId = orderService.updateOrderState(req, "ceo@gmail.com");

		// then
		verify(orderRepository, times(1)).save(captor.capture());
		assertEquals(COOKING, captor.getValue().getOrderState());

	}

	@Test
	@DisplayName("주문 상태 변경 실패 - 취소된 주문은 변경이 불가합니다.")
	void updateOrderStateFail_cannotChangeCanceledOrder() {
		// given
		CeoUpdate req = CeoUpdate.builder()
			.orderId(1L).storeId(1L)
			.orderState(COOKING).build();
		Member ceo = Member.builder()
			.id(1L).build();
		Member user = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(ceo).build();
		Orders order = Orders.builder()
			.id(1L).member(user).orderCancel(true)
			.orderState(BEFORE_COOKING).build();
		order.setRegDt(LocalDateTime.now());

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(ceo));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.updateOrderState(req, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_CHANGE_CANCELED_ORDER, exception.getErrorCode());
	}

	@Test
	@DisplayName("주문 상태 변경 실패 - 현재(오늘) 주문 건이 아닙니다.")
	void updateOrderStateFail_notTodayOrder() {
		// given
		CeoUpdate req = CeoUpdate.builder()
			.orderId(1L).storeId(1L)
			.orderState(COOKING).build();
		Member ceo = Member.builder()
			.id(1L).build();
		Member user = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(ceo).build();
		Orders order = Orders.builder()
			.id(1L).member(user).orderCancel(false)
			.orderState(BEFORE_COOKING).build();
		order.setRegDt(LocalDateTime.now().minusDays(1));

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(ceo));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.updateOrderState(req, "ceo@gmail.com"));

		// then
		assertEquals(NOT_TODAY_ORDER, exception.getErrorCode());
	}

	@Test
	@DisplayName("주문 상태 변경 실패 - 같은 주문 상태로 변경이 불가합니다.")
	void updateOrderStateFail_cannotChangeSameOrderState() {
		// given
		CeoUpdate req = CeoUpdate.builder()
			.orderId(1L).storeId(1L)
			.orderState(COOKING).build();
		Member ceo = Member.builder()
			.id(1L).build();
		Member user = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(ceo).build();
		Orders order = Orders.builder()
			.id(1L).member(user).orderCancel(false)
			.orderState(COOKING).build();
		order.setRegDt(LocalDateTime.now());

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(ceo));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.updateOrderState(req, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_CHANGE_SAME_ORDER_STATE, exception.getErrorCode());
	}

	@Test
	@DisplayName("주문 상태 변경 실패 - 완료된 주문은 변경이 불가합니다.")
	void updateOrderStateFail_cannotChangeCompleteOrderState() {
		// given
		CeoUpdate req = CeoUpdate.builder()
			.orderId(1L).storeId(1L)
			.orderState(COOKING).build();
		Member ceo = Member.builder()
			.id(1L).build();
		Member user = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(ceo).build();
		Orders order = Orders.builder()
			.id(1L).member(user).orderCancel(false)
			.orderState(PICKUP_COMPLETE).build();
		order.setRegDt(LocalDateTime.now());

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(ceo));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.updateOrderState(req, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_CHANGE_COMPLETE_ORDER_STATE, exception.getErrorCode());
	}

	@Test
	@DisplayName("주문 상태 변경 실패 - 전 주문 상태로 변경이 불가합니다.")
	void updateOrderStateFail_cannotChangePreviousOrderState() {
		// given
		CeoUpdate req = CeoUpdate.builder()
			.orderId(1L).storeId(1L)
			.orderState(COOKING).build();
		Member ceo = Member.builder()
			.id(1L).build();
		Member user = Member.builder()
			.id(2L).build();
		Store store = Store.builder()
			.id(1L).member(ceo).build();
		Orders order = Orders.builder()
			.id(1L).member(user).orderCancel(false)
			.orderState(PICKUP_REQ).build();
		order.setRegDt(LocalDateTime.now());

		given(memberRepository.findByUserId(anyString()))
			.willReturn(Optional.of(ceo));
		given(storeRepository.findById(anyLong()))
			.willReturn(Optional.of(store));
		given(orderRepository.findById(anyLong()))
			.willReturn(Optional.of(order));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> orderService.updateOrderState(req, "ceo@gmail.com"));

		// then
		assertEquals(CANNOT_CHANGE_PREVIOUS_ORDER_STATE, exception.getErrorCode());
	}

}