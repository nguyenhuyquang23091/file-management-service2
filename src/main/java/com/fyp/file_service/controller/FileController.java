package com.fyp.file_service.controller;


import com.fyp.file_service.dto.response.ApiResponse;
import com.fyp.file_service.dto.response.FileResponse;
import com.fyp.file_service.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;
    @PostMapping("/media/upload")
    public ApiResponse<FileResponse> uploadImage
            (@RequestParam("file") MultipartFile file

           ) throws  IOException {
        return ApiResponse.<FileResponse>builder()
                .result(fileService.uploadFile(file ))
                .build();
    }


}
