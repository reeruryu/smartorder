package com.example.smartorder.repository;

import com.example.smartorder.entity.Store;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	Page<Store> findAll(Pageable pageable);
	Optional<Store> findByStoreName(String storeName);

	@Query(value = "select s from Store s "
		+ "where s.storeName = :storeName "
		+ "and s.id <> :storeId")
	Optional<Store> existsByStoreNameExceptId(String storeName, Long storeId);

}
