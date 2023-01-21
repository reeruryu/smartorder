package com.example.smartorder.repository;

import com.example.smartorder.entity.TransactionConvPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionConvPayRepository extends JpaRepository<TransactionConvPay, Long> {

}

