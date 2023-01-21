package com.example.smartorder.repository;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.TransactionConvPay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionConvPayRepository extends JpaRepository<TransactionConvPay, Long> {

	@Query(value = "select tc from TransactionConvPay tc "
		+ "where tc.convPay.member = :member")
	Page<TransactionConvPay> findAllByMember(Member member, Pageable pageable);
}

