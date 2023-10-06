package com.maindark.livestream.txYun;

import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class TxYunStorageService {
    @Value("${tengxun.bucket.name}")
    private String bucketName;
    @Value("${tengxun.bucket.name.folder}")
    private String folder;
    @Value("${tengxun.host}")
    private String host;

    @Resource
    private COSClient cosClient;

    public void uploadFile(MultipartFile file,String fileName) {
        File fileObj = convertMultiPartFileToFile(file);
//      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        cosClient.putObject(new PutObjectRequest(bucketName, folder+fileName, fileObj));
        fileObj.delete();
    }

    public void deleteFile(String fileName){
        String key = folder + fileName;
        cosClient.deleteObject(bucketName, key);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
            throw new GlobalException(CodeMsg.IMG_UPLOAD_ERROR);
        }
        return convertedFile;
    }

}
