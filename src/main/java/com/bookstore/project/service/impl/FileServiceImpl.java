package com.bookstore.project.service.impl;

import com.bookstore.project.config.AppConfig;
import com.bookstore.project.entity.User;
import com.bookstore.project.exception.RestException;
import com.bookstore.project.request.FileInfo;
import com.bookstore.project.service.FileService;
import com.bookstore.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private UserService userService;

    @Autowired
    private AppConfig appConfig;


    @Override
    public FileInfo uploadImage(MultipartFile multipartFile) {

        User currentUser = userService.getCurrentUser();

        if (multipartFile.isEmpty()) {
            throw RestException.badRequest("Failed to upload empty file");
        }

        String name = Optional.ofNullable(multipartFile.getOriginalFilename())
                .orElseThrow(() -> RestException.badRequest("File name is not provided"));

        String extension = getFileExtension(name)
                .orElseThrow(() -> RestException.badRequest("File extension is not supported"));

        List<String> supportedExtensions = List.of("jpg", "jpeg", "png");
        if (!supportedExtensions.contains(extension)) {
            throw RestException.badRequest("File extension is not supported");
        }

        String path = String.format("%s/users/%s", appConfig.getUploadPath(), currentUser.getId());
        return uploadFile(path, multipartFile);
    }
    
    @Override
    public boolean deleteImage(String path) {
        // Lấy người dùng hiện tại từ Optional<User>
        User currentUser = userService.getCurrentUser(); // Ném ngoại lệ nếu không tìm thấy người dùng

        String checkPath = String.format("%s/users/%s", appConfig.getUploadPath(), currentUser.getId());

        // Kiểm tra xem đường dẫn có hợp lệ không
        if (StringUtils.isEmpty(path) || !path.contains(checkPath)) {
            throw RestException.badRequest("Cannot delete file");
        }

        return deleteFile(path); // Gọi phương thức để xóa file
    }

    @Override
    public FileInfo uploadFileExcel(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw RestException.badRequest("Failed to upload empty file");
        }

        String name = multipartFile.getOriginalFilename();
        String extension = getFileExtension(name).orElseThrow(() -> RestException.badRequest("File extension is not supported"));

        if (!"xlsx".equals(extension)) {
            throw RestException.badRequest("File extension is not supported");
        }

        String path = appConfig.getUploadPath() + "/excel";

        return uploadFile(path, multipartFile);
    }

    @Override
    public boolean deleteFile(String path) {
        try {
            log.debug("Delete file {}", appConfig.getUploadDir() + path);
            File file = new File(appConfig.getUploadDir() + path);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public FileInfo uploadFile(String path, MultipartFile file) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu nó chưa tồn tại
            }

            // Lưu file vào thư mục
            File newFile = new File(directory, file.getOriginalFilename());
            file.transferTo(newFile); // Lưu file vào hệ thống

            // Trả về thông tin file đã tải lên
            return new FileInfo(newFile.getName(), newFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    private Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains(".")).map(f -> f.toLowerCase().substring(filename.lastIndexOf(".") + 1));
    }

    private void mkdirs(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
