package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class FileService {
    private final UserService userService;
    private final FileMapper fileMapper;

    public FileService(UserService userService, FileMapper fileMapper) {
        this.userService = userService;
        this.fileMapper = fileMapper;
    }

    public boolean isFileAvailable(String filename, Integer userId){ return fileMapper.getFileByNameAndUserId(filename, userId) == null; };

    public List<File> getFiles(){
        Integer userId = userService.getCurrentUser().getUserId();
        return fileMapper.getFiles(userId);
    }

    public File getFile(Integer fileId){
        return fileMapper.getFile(fileId);
    }

    public int uploadFile(MultipartFile uploadedFile) throws IOException{
        Integer userId = userService.getCurrentUser().getUserId();

        if (isFileAvailable(uploadedFile.getOriginalFilename(), userId)) {
            File file = new File(
                    null,
                    uploadedFile.getOriginalFilename(),
                    uploadedFile.getContentType(),
                    uploadedFile.getSize()+" bytes",
                    uploadedFile.getBytes(),
                    userId
            );

            return fileMapper.insert(file);
        } else return -1;


    }


    public int deleteFile(int fileId){
        return fileMapper.delete(fileId);
    }
}
