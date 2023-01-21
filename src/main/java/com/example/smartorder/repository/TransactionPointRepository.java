package com.example.smartorder.repository;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.TransactionPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPointRepository extends JpaRepository<TransactionPoint, Long> {
	@Query(value = "select tp from TransactionPoint tp "
		+ "where tp.point.member = :member")
	Page<TransactionPoint> findAllByMember(Member member, Pageable pageable);
}

