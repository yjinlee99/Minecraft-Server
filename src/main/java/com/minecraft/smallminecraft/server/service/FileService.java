package com.minecraft.smallminecraft.server.service;

import com.minecraft.smallminecraft.server.dtos.FileInfoDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String uploadDir = "C:/User/upload";

    @Transactional
    public FileInfoDto uploadFile(String username, String servername, MultipartFile file, String info) {
        String originalFileName = file.getOriginalFilename();
        String mimeType = file.getContentType();

        // 저장 파일명 변경
        String newFileName = username + "-" +  servername + "-" + info;
        Path uploadPath = Paths.get(uploadDir);

        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory!", e);
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

            return fileInfoDto;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed!", e);
        }
    }
}
