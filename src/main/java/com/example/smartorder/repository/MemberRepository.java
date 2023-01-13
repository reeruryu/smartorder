package com.example.smartorder.repository;

import com.example.smartorder.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

	Optional<Member> findByUserId(String userId);
	boolean existsByUserId(String userId);

	Optional<Member> findByEmailAuthKey(String uuid);



//	List<Member> findAllByCeoYn(boolean ceoYn);
//
//	List<Member> findAllByUserRole(String userRole);
}
