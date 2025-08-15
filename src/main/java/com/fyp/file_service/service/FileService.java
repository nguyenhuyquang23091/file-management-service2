package com.fyp.file_service.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.file_service.dto.FileInfo;
import com.fyp.file_service.dto.response.FileResponse;
import com.fyp.file_service.entity.FileManagement;
import com.fyp.file_service.mapper.FileManagementMapper;
import com.fyp.file_service.repository.FileManagementRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class FileService {
    Cloudinary cloudinary;
    @Qualifier("fileManagementMapperImpl")
    FileManagementMapper managementMapper;
    FileManagementRepository repository;
    ObjectMapper objectMapper;


    public FileResponse uploadFile(MultipartFile file ) throws  IOException{
        Map<?, ? > cloudinaryResponse = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName));
        log.info("Raw Cloudinary response: {}", cloudinaryResponse);

        FileInfo fileInfo = objectMapper.convertValue(cloudinaryResponse, FileInfo.class);
            log.info("Successfully converted to FileInfo: {}", fileInfo);

        FileManagement fileManagement = managementMapper.toFileManagement(fileInfo);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setOwnerId(userId);
        repository.save(fileManagement);

        return FileResponse.builder().
                url(fileInfo.getSecureUrl())
                        .originalFilename(file.getOriginalFilename()).
                build();
    }

}
