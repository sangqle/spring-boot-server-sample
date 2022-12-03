package com.example.springboot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/")
@RestController
public class UploadController {
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file")MultipartFile file) {
        System.err.println(file.getOriginalFilename());
        return "uploaded";
    }
}
