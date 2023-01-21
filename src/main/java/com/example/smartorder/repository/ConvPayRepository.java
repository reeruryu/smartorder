package com.example.smartorder.repository;

import com.example.smartorder.entity.ConvPay;
import com.example.smartorder.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvPayRepository extends JpaRepository<ConvPay, Long> {

	ConvPay findByMember(Member member);
}
