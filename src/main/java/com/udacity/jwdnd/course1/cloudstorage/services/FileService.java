package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FilesMapper filesMapper;
    private UserService userService;

    public FileService(FilesMapper filesMapper, UserService userService) {
        this.filesMapper = filesMapper;
        this.userService = userService;
    }

    public String saveFile(Authentication authentication, MultipartFile file) {
        User user = userService.getUserFromAuthentication(authentication).orElseThrow();
        if (filesMapper.getFileByFileName(file.getOriginalFilename(), user.getUserId()).isPresent()) {
            return "File with name '" + file.getOriginalFilename() + "' already exists!";
        }
        Files files = null;
        try {
            files = new Files(null, file.getOriginalFilename(), file.getContentType(), "" + file.getSize(), user.getUserId(), file.getBytes());
            filesMapper.insert(files);
            return "File '" + file.getOriginalFilename() + "' created.";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public List<Files> getFilesForUser(Authentication authentication) {
        User user = userService.getUserFromAuthentication(authentication).orElseThrow();
        return filesMapper.getFilesByUserId(user.getUserId());
    }

    public Files getFileById(Integer fileId) {
        return filesMapper.getFileById(fileId);
    }

    public void deleteFile(Integer fileId) {
        filesMapper.deleteFileById(fileId);
    }
}
