package com.example.smartorder.member.repository;

import com.example.smartorder.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

	Optional<Member> findByEmailAuthKey(String uuid);

	List<Member> findAllByCeoYn(boolean ceoYn);

	List<Member> findAllByUserRole(String userRole);
}
