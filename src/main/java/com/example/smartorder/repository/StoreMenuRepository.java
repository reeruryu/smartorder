package com.example.smartorder.repository;

import com.example.smartorder.entity.Menu;
import com.example.smartorder.entity.StoreMenu;
import com.example.smartorder.type.SaleState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMenuRepository extends JpaRepository<StoreMenu, Long> {

	void deleteAllByMenu(Menu menu);
	List<StoreMenu> findAllBySaleState(SaleState saleState);


}
