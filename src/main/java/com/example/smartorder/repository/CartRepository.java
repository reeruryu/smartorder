package com.example.smartorder.repository;

import com.example.smartorder.entity.Cart;
import com.example.smartorder.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByMember(Member member);

}
