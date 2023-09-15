package com.maindark.livestream.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;
    @Value("${application.bucket.name.folder}")
    private String folder;

    @Value("${application.bucket.host}")
    private String host;
    @Resource
   private AmazonS3 amazonS3;

    public void uploadFile(MultipartFile file,String fileName) {
        File fileObj = convertMultiPartFileToFile(file);
//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, folder+fileName, fileObj));
        fileObj.delete();
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            throw new GlobalException(CodeMsg.IMG_UPLOAD_ERROR);
        }
    }


    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, folder+fileName);

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
