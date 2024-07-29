package com.minecraft.smallminecraft.server.repository;


import com.minecraft.smallminecraft.server.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Integer> {

    Server findByMember_IdAndServerName(int memberId, String servername);
}
