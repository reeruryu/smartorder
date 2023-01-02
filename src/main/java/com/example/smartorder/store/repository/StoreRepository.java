package com.example.smartorder.store.repository;

import com.example.smartorder.store.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findByStoreName(String storeName);
}
