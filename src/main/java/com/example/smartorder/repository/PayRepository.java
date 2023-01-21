package com.example.smartorder.repository;

import com.example.smartorder.entity.Orders;
import com.example.smartorder.entity.Pay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {

	List<Pay> findAllByOrder(Orders order);
}
