package com.minecraft.smallminecraft.server.service;

import com.minecraft.smallminecraft.response.ErrorResponse;
import com.minecraft.smallminecraft.server.dtos.FileInfoDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Transactional
    public boolean deleteServer(String username, String servername,  String info) {
        String fileName = username + "-" +  servername + "-map";
        Path filePath = Paths.get(uploadDir, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("맵파일 삭제 완료: " + fileName);
            } else {
                log.info("맵파일이 존재하지 않습니다: " + fileName);
            }
        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: " + fileName, e);
            return false;
        }

        fileName = username + "-" +  servername + "-info";
        filePath = Paths.get(uploadDir, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("인포 파일 삭제 완료: " + fileName);
                return true;
            } else {
                log.info("인포파일이 존재하지 않습니다: " + fileName);
            }
        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: " + fileName, e);
            return false;
        }

        return true;
    }

    @Transactional
    public ResponseEntity<Object> exportFile(String username, String servername, String info) {
        String fileName = username + "-" +  servername + "-" + info;

        Path filePath = Paths.get(uploadDir, fileName);

        if (Files.exists(filePath)) {
            try {
                Resource resource = new InputStreamResource(Files.newInputStream(filePath));
                String contentType = Files.probeContentType(filePath);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(Files.size(filePath))
                        .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                        .body(resource);

            } catch (IOException e) {
                log.error("파일 내보내기 중 오류 발생: {}", fileName, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("파일 내보내기 중 오류 발생"));
            }
        } else {
            log.info("파일이 존재하지 않습니다: {}", fileName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("파일이 존재하지 않습니다"));
        }
    }
}
