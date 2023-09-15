package com.maindark.livestream.controller;

import com.maindark.livestream.aws.StorageService;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class UploadFileController {


    @Resource
    private StorageService storageService;

    @PostMapping("/upload")
    public Result<String>  upLoadFile(@RequestParam("file")MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUIDUtil.uuid() + suffixName;
        storageService.uploadFile(file,fileName);
        return Result.success(fileName);
    }

    @DeleteMapping("/delete/{fileName}")
    public Result<Boolean> deleteFile(@PathVariable String fileName){
       storageService.deleteFile(fileName);
        return Result.success(true);
    }

}
