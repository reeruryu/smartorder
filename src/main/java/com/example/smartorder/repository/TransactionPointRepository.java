package com.example.smartorder.repository;

import com.example.smartorder.entity.TransactionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionPointRepository extends JpaRepository<TransactionPoint, Long> {

}

