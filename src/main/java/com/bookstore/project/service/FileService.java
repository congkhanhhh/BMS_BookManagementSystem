package com.bookstore.project.service;

import com.bookstore.project.request.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileInfo uploadFileExcel(MultipartFile multipartFile);
    FileInfo uploadFile(String path, MultipartFile multipartFile);
    boolean deleteFile(String path);
    FileInfo uploadImage(MultipartFile multipartFile);
    boolean deleteImage(String path);
}
