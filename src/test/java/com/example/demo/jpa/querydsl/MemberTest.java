package com.example.demo.jpa.querydsl;

import com.example.demo.jpa.querydsl.entity.Member;
import com.example.demo.jpa.querydsl.entity.Order;
import com.example.demo.jpa.querydsl.entity.Team;
import com.example.demo.jpa.querydsl.exception.TeamNotFoundException;
import com.example.demo.jpa.querydsl.service.MemberService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.example.demo.jpa.querydsl.entity.QMember.member;
import static com.example.demo.jpa.querydsl.entity.QTeam.team;
import static com.example.demo.jpa.querydsl.entity.QOrder.order;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    MemberService memberService;

    @BeforeEach
    public void setUp() {
        // 팀 생성
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        // 회원 생성
        Member member1 = new Member("member1", 20, teamA);
        Member member2 = new Member("member2", 30, teamA);
        Member member3 = new Member("member3", 40, teamB);
        Member member4 = new Member("member4", 50, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 주문 생성
        Order order1 = new Order("order1", member1);
        Order order2 = new Order("order2", member2);
        Order order3 = new Order("order3", member3);
        Order order4 = new Order("order4", member1);
        em.persist(order1);
        em.persist(order2);
        em.persist(order3);
        em.persist(order4);
    }

    // 이전에 작성한 테스트 케이스들...

    @Test
    public void testThreeTableJoin() {
        List<Tuple> result = queryFactory
                .select(member.username, team.name, order.orderNumber)
                .from(order)
                .join(order.member, member)
                .join(member.team, team)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            String teamName = tuple.get(team.name);
            String orderNumber = tuple.get(order.orderNumber);

            System.out.println("username: " + username + ", team: " + teamName + ", orderNumber: " + orderNumber);
        }

        // 결과 검증
        assertThat(result).hasSize(4);
        assertThat(result).extracting(tuple -> tuple.get(member.username))
                .containsExactlyInAnyOrder("member1", "memb" +
                        "er1", "member2", "member3");
    }

    @Test
    public void testCreateMemberWithNonExistingTeam() {
        // given
        String username = "newMember";
        int age = 25;
        String teamName = "NonExistingTeam";

        // when & then
        assertThrows(TeamNotFoundException.class, () -> {
            memberService.createMember(username, age, teamName);
        });
    }
}