package com.maindark.livestream.upyun;

import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.upyun.FormUploader;
import com.upyun.Params;
import com.upyun.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpYunService {

    @Resource
    UpYunConfig upYunConfig;

    public Result uploadImg(byte[] datas,String fileName) {
        //init uploader
        FormUploader uploader = new FormUploader(upYunConfig.getBucketName(),upYunConfig.getUsername(),upYunConfig.getPassword());
        // init param map
        final Map<String,Object> paramsMap = new HashMap<>();
        // add SAVE_KEY
        paramsMap.put(Params.SAVE_KEY,"/live-stream" + fileName);
        paramsMap.put(Params.X_GMKERL_THUMB,"/fw/300/unsharp/true/quality/80/format/png");
        Result result = null;
        try{
            result = uploader.upload(paramsMap,datas);
        }catch (Exception e){
            throw new GlobalException(CodeMsg.IMG_UPLOAD_ERROR);
        }
        return result;
    }
}
