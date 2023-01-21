package com.example.smartorder.repository;

import com.example.smartorder.entity.Member;
import com.example.smartorder.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

	Point findByMember(Member member);
}

