package com.example.demo.jpa.querydsl.repository;


import com.example.demo.jpa.querydsl.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}