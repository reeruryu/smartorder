package com.example.smartorder.repository;

import com.example.smartorder.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUserId(String userId);
	boolean existsByUserId(String userId);
	Optional<Member> findByEmailAuthKey(String uuid);
	Page<Member> findByUserIdContaining(String userId, Pageable pageable);
}
