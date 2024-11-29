package com.example.demo.jpa.querydsl.service;


import com.example.demo.jpa.querydsl.entity.Member;
import com.example.demo.jpa.querydsl.entity.Team;
import com.example.demo.jpa.querydsl.exception.TeamNotFoundException;
import com.example.demo.jpa.querydsl.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void createMember(String username, int age, String teamName) {
        Team team;
        try {
            team = em.createQuery("select t from Team t where t.name = :name", Team.class)
                    .setParameter("name", teamName)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new TeamNotFoundException("Team not found: " + teamName);
        }

        Member member = new Member(username, age, team);
        memberRepository.save(member);
    }
}