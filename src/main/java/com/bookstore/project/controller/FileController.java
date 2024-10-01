package com.bookstore.project.controller;

import com.bookstore.project.request.FileInfo;
import com.bookstore.project.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/public/files")
@Tag(name = "Files", description = "File Public APIs")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/images", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload image", security = {@SecurityRequirement(name = "bearerAuth")})
    public FileInfo uploadImage(@RequestParam MultipartFile file) {
        return fileService.uploadImage(file);
    }

    @PostMapping(value = "/excel", consumes = {"multipart/form-data"})
    @Operation(summary = "Upload file excel", security = {@SecurityRequirement(name = "bearerAuth")})
    public FileInfo uploadFileExcel(@RequestParam MultipartFile file) {
        return fileService.uploadFileExcel(file);
    }

    @DeleteMapping(value = "/images")
    @Operation(summary = "Delete image", security = {@SecurityRequirement(name = "bearerAuth")})
    public boolean deleteImage(@RequestParam String path) {
        return fileService.deleteImage(path);
    }

    @DeleteMapping(value = "/file")
    @Operation(summary = "Delete file", security = {@SecurityRequirement(name = "bearerAuth")})
    public boolean deleteFile(@RequestParam String path){
        return fileService.deleteFile(path);
    }
}

