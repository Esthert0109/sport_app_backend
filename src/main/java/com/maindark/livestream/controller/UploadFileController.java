package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploadImg")
public class UploadFileController {




    @GetMapping("/add")
    public Result<String>  upLoadFile(@RequestParam("file")MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUIDUtil.uuid() + suffixName;
        byte[] bytes = file.getBytes();
        return Result.success(fileName);
    }

}
