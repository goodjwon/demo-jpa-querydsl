package com.example.demo.jpa.querydsl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "orders") // 테이블 이름을 'orders'로 지정
@NoArgsConstructor
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Order(String orderNumber, Member member) {
        this.orderNumber = orderNumber;
        if (member != null) {
            changeMember(member);
        }
    }

    // 연관 관계 편의 메서드
    public void changeMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
}