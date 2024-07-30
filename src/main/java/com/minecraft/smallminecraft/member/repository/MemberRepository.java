package com.minecraft.smallminecraft.member.repository;


import com.minecraft.smallminecraft.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {


    Member findByUsername(String username);
    Member findByEmail(String email);
}
