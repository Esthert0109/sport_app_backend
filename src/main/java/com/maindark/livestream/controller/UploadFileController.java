package com.maindark.livestream.controller;

import com.maindark.livestream.aws.StorageService;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.txYun.TxYunStorageService;
import com.maindark.livestream.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin
public class UploadFileController {


    @Resource
    private StorageService storageService;

    @Resource
    private TxYunStorageService txYunStorageService;

    @PostMapping("/upload/aws")
    public Result<String>  upLoadFile(@RequestParam("file")MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUIDUtil.uuid() + suffixName;
        storageService.uploadFile(file,fileName);
        return Result.success(fileName);
    }

    @DeleteMapping("/delete/aws/{fileName}")
    public Result<Boolean> deleteFile(@PathVariable String fileName){
        storageService.deleteFile(fileName);
        return Result.success(true);
    }

    /**
     * upload head
     */


    @PostMapping("/upload/tx")
    public Result<String>  upLoadFileByTx(@RequestParam("file")MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUIDUtil.uuid() + suffixName;
        String imageUrl = txYunStorageService.uploadFile(file,fileName);
        return Result.success(imageUrl);
    }
    /**
     *  upload cover
     */

    @PostMapping("/upload/cover")
    public Result<String>  upLoadCoverByTx(@RequestParam("file")MultipartFile file){
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        fileName = UUIDUtil.uuid() + suffixName;
        String imageUrl = txYunStorageService.uploadCover(file,fileName);
        return Result.success(imageUrl);
    }


    @DeleteMapping("/delete/tx/{fileName}")
    public Result<Boolean> deleteFileByTx(@PathVariable String fileName){
        txYunStorageService.deleteFile(fileName);
        return Result.success(true);
    }

}
