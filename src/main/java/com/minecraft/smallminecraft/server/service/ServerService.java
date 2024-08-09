package com.minecraft.smallminecraft.server.service;

import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.server.dtos.AddServerDTO;
import com.minecraft.smallminecraft.server.entity.Server;
import com.minecraft.smallminecraft.server.repository.ServerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ServerService {

    private final MemberRepository memberRepository;
    private final ServerRepository serverRepository;
    public ServerService(MemberRepository memberRepository, ServerRepository serverRepository) {
        this.memberRepository = memberRepository;
        this.serverRepository = serverRepository;
    }

    @Transactional
    public ResponseEntity<Object> addServer(AddServerDTO addServerDTO, String username) {
        Member member = memberRepository.findByUsername(username);
        if(member == null)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("존재하지 않는 회원입니다."));
        }

        Server findServer = serverRepository.findByNameAndMember(addServerDTO.getServername(), member);

        if(findServer != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 이름입니다."));
        }

        Server server = new Server();
        server.setName(addServerDTO.getServername());
        server.setMember(member);

        Server savedServer = serverRepository.save(server);
        log.info("서버 생성 완료 : {}",savedServer.getName());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
