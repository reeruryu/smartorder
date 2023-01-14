package com.example.smartorder.repository;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Store;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
	Page<Orders> findByMemberAndRegDtBetween(Member member, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	@Query(value = "select distinct o " +
		"from Orders o " +
		"where o.store = :store " +
		"and o.payState <> 'BEFORE_PAY' " +
		"and o.regDt > :startDate " +
		"and o.regDt < :endDate")
	Page<Orders> findByStoreAndRegDtBetween(Store store, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
