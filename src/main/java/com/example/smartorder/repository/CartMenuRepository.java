package com.example.smartorder.repository;

import com.example.smartorder.entity.Cart;
import com.example.smartorder.entity.CartMenu;
import com.example.smartorder.entity.StoreMenu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {

	CartMenu findByCartAndStoreMenu(Cart cart, StoreMenu storeMenu);

	List<CartMenu> findAllByCart(Cart cart);
}
