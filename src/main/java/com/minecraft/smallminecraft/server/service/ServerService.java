package com.minecraft.smallminecraft.server.service;


import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.server.dto.ServerAddDTO;
import com.minecraft.smallminecraft.server.dto.ServerLoadResponseDTO;
import com.minecraft.smallminecraft.server.dto.ServerSaveDTO;
import com.minecraft.smallminecraft.server.entity.Server;
import com.minecraft.smallminecraft.server.repository.ServerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.minecraft.smallminecraft.response.ErrorResponse;

@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final MemberRepository memberRepository;

    public ServerService(ServerRepository serverRepository, MemberRepository memberRepository) {
        this.serverRepository = serverRepository;
        this.memberRepository = memberRepository;
    }

    public ResponseEntity<Object> addServer(String username, ServerAddDTO serverAddDTO) {

        Member member = memberRepository.findByUsername(username);
        Server server = serverRepository.findByMember_IdAndServerName(member.getId(), serverAddDTO.getServer_name());

        if(server != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 서버 이름입니다."));
        }

        Server newServer = new Server();
        newServer.setMember(member);
        newServer.setServerName(serverAddDTO.getServer_name());
        newServer.setPassword(serverAddDTO.getPassword());
        newServer.setDifficulty(serverAddDTO.getDifficulty());
        newServer.setDate(0);
        newServer.setTime(0);

        serverRepository.save(newServer);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    public ResponseEntity<Object> loadServer(int serverId) {
        Server server = serverRepository.findById(serverId).orElse(null);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("해당 서버 데이터가 존재하지 않습니다."));
        }

        ServerLoadResponseDTO dto = new ServerLoadResponseDTO();

        dto.setDate(server.getDate());
        dto.setDifficulty(server.getDifficulty());
        dto.setTime(server.getTime());

        return ResponseEntity.status(HttpStatus.OK)
                .body(dto);

    }


    @Transactional
    public ResponseEntity<Object> saveServer(@NotBlank int server_ID, ServerSaveDTO dto) {
        Server server = serverRepository.findById(server_ID).orElse(null);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("해당 서버 데이터가 존재하지 않습니다."));
        }

        server.setDate(dto.getDate());
        server.setTime(dto.getTime());
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Transactional
    public ResponseEntity<Object> deleteServer(int serverId) {
        Server server = serverRepository.findById(serverId).orElse(null);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("해당 서버 데이터가 존재하지 않습니다."));
        }

        serverRepository.delete(server);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
