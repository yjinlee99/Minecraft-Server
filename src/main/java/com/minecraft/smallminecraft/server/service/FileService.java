package com.minecraft.smallminecraft.server.service;

import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.server.dtos.FileInfoDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileService {

    private final String uploadDir = "C:/User/upload";

    @Transactional
    public ResponseEntity<Object> uploadFile(String username, String servername, MultipartFile file, String info) {

        // 저장 파일명 변경
        String newFileName = username + "-" +  servername + "-" + info;
        Path uploadPath = Paths.get(uploadDir);

        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                log.info("upload directory를 만들 수 없습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("서버 오류로 인한 파일 저장 실패"));
            }
        }

        try {
            // 파일 저장
            Path filePath = uploadPath.resolve(newFileName);
            file.transferTo(filePath.toFile());

            // 파일 정보 DTO 생성
            FileInfoDto fileInfoDto = new FileInfoDto();
            fileInfoDto.setNewFileName(newFileName);
            fileInfoDto.setFilePath(filePath.toString());

            log.info("파일 저장 완료");
            return ResponseEntity.status(HttpStatus.OK)
                    .build();

        } catch (IOException e) {
            log.info("서버 오류로 인한 파일 저장 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("서버 오류로 인한 파일 저장 실패"));
        }
    }
}
