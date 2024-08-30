package com.minecraft.smallminecraft.server.service;

import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.server.dtos.AddServerDTO;
import com.minecraft.smallminecraft.server.dtos.AddServerResponseDTO;
import com.minecraft.smallminecraft.server.dtos.FileInfoDto;
import com.minecraft.smallminecraft.server.entity.Server;
import com.minecraft.smallminecraft.server.repository.ServerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ServerService {

    private final MemberRepository memberRepository;
    private final ServerRepository serverRepository;
    private final FileService fileService;
    public ServerService(MemberRepository memberRepository, ServerRepository serverRepository, FileService fileService) {
        this.memberRepository = memberRepository;
        this.serverRepository = serverRepository;
        this.fileService = fileService;
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

        AddServerResponseDTO dto = new AddServerResponseDTO(savedServer.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(dto);
    }

    //맵 파일 저장
    public ResponseEntity<Object> updateMap(String username, String servername, MultipartFile file) {
        Member member = memberRepository.findByUsername(username);
        Server server = serverRepository.findByNameAndMember(servername, member);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("서버가 존재하지 않습니다."));
        }
        return fileService.uploadFile(username, servername, file, "map");
    }

    //info 파일 저장
    public ResponseEntity<Object> updateInfo(String username, String servername, MultipartFile file) {
        Member member = memberRepository.findByUsername(username);
        Server server = serverRepository.findByNameAndMember(servername, member);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("서버가 존재하지 않습니다."));
        }
        return fileService.uploadFile(username, servername, file, "info");
    }

    public ResponseEntity<Object> deleteServer(String username, String servername) {
        Member member = memberRepository.findByUsername(username);
        Server server = serverRepository.findByNameAndMember(servername, member);

        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("서버가 존재하지 않습니다."));
        }

        boolean result = fileService.deleteServer(username, servername, "map");
        if(result) {
            serverRepository.delete(server);
            log.info("서버 삭제 완료");
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("서버 오류로 삭제되지 않았습니다."));
        }
    }

    public ResponseEntity<Object> exportMap(String username, String servername) {
        Member member = memberRepository.findByUsername(username);
        Server server = serverRepository.findByNameAndMember(servername, member);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("서버가 존재하지 않습니다."));
        }
        return fileService.exportFile(username, servername, "map");
    }

    public ResponseEntity<Object> exportInfo(String username, String servername) {
        Member member = memberRepository.findByUsername(username);
        Server server = serverRepository.findByNameAndMember(servername, member);
        if(server == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("서버가 존재하지 않습니다."));
        }
        return fileService.exportFile(username, servername, "info");
    }
}
