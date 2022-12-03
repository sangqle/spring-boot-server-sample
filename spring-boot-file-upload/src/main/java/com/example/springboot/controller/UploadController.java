package com.example.springboot.controller;

import com.example.springboot.storage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/")
@RestController
public class UploadController {

    @Autowired
    FileSystemStorageService storageService;

    @GetMapping("/upload/files")
    public String listUploadedFiles() throws IOException {

        List<String> filePaths =
                storageService.loadAll()
                        .map(path ->  path.getFileName().toString())
                        .collect(Collectors.toList());

        return filePaths.toString();
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        System.err.println(file.getOriginalFilename());
        storageService.store(file);
        return "uploaded";
    }
}
