package com.example.demo.jpa.querydsl.repository;


import com.example.demo.jpa.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}