package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(int userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public File getFile(int fileId){
        return fileMapper.getFile(fileId);
    }

    public int uploadFile(MultipartFile uploadFile, int userId) throws IOException {
        File file = new File();
        file.setFileName(uploadFile.getOriginalFilename());
        file.setUserId(userId);
        file.setFileData(uploadFile.getBytes());
        file.setFileSize(String.valueOf(uploadFile.getSize()));
        file.setContentType(uploadFile.getContentType());
        return fileMapper.insert(file);
    }

    public boolean deleteFile(int fileId){
        return fileMapper.deleteByFileId(fileId);
    }

    public boolean isExistFileName(String fileName, int userId){
        return getFiles(userId).stream().map(f -> f.getFileName()).anyMatch(name -> name.equals(fileName));
    }
}
