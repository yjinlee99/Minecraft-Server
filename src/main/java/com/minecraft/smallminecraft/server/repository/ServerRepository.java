package com.minecraft.smallminecraft.server.repository;

import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Integer> {
    public Server findByNameAndMember(String name, Member member);
}
