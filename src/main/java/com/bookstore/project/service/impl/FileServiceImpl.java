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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private UserService userService;

    @Autowired
    private AppConfig appConfig;

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
    public FileInfo uploadFile(String path, MultipartFile multipartFile) {

        FileInfo fileInfo = new FileInfo();
        if (multipartFile.isEmpty()) {
            throw RestException.badRequest("Failed to upload empty file");
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String extension = getFileExtension(originalFilename).orElseThrow(() -> RestException.badRequest("File extension is not supported"));

        try {
            String name = UUID.randomUUID() + "." + extension;
            mkdirs(appConfig.getUploadDir() + path);

            fileInfo.setName(originalFilename);
            fileInfo.setSize(multipartFile.getSize() / 1024);
            fileInfo.setUrl(path + "/" + name);

            log.debug("Write file {} to {}", name, appConfig.getUploadDir() + path);
            Files.copy(multipartFile.getInputStream(), Paths.get(appConfig.getUploadDir() + path + "/" + name), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw RestException.internalServerError();
        }

        return fileInfo;
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

    @Override
    public FileInfo uploadImage(MultipartFile multipartFile) {

        User currentUser = userService.getCurrentUser();

        if (multipartFile.isEmpty()) {
            throw RestException.badRequest("Failed to upload empty file");
        }

        String name = multipartFile.getOriginalFilename();
        String extension = getFileExtension(name).orElseThrow(() -> RestException.badRequest("File extension is not supported"));

        if (!List.of("jpg", "jpeg", "png").contains(extension)) {
            throw RestException.badRequest("File extension is not supported");
        }

        String path = String.format("%s/users/%s", appConfig.getUploadPath(), currentUser.getId());
        return uploadFile(path, multipartFile);
    }

    @Override
    public boolean deleteImage(String path) {

        User currentUser = userService.getCurrentUser();
        String checkPath = String.format("%s/users/%s", appConfig.getUploadPath(), currentUser.getId());

        // warning
        if (StringUtils.isEmpty(path) || !path.contains(checkPath)) {
            throw RestException.badRequest("Cannot delete file");
        }

        return deleteFile(path);
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
