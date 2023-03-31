package com.himedia.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.himedia.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByUsername(String username);
	Optional<Member> findByToken(String token);
}
