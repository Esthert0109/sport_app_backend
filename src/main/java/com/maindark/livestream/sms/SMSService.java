package com.maindark.livestream.sms;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.SMSKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.UUIDUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
@Service
@Slf4j
public class SMSService {

    @Resource
    SMSConfig smsConfig;

    @Resource
    RedisService redisService;
    public  String sendSMS(String mobileNumber) {
        // 随机的四位数OTP
        Random random = new Random();
        int OTPNumber = random.nextInt(9000) + 1000;
        String messageContent = smsConfig.getContent().replace("{code}",String.valueOf(OTPNumber)) ;
        // 自动生成一个 referenceID
        String referenceID = UUIDUtil.uuid();
        String param = "apiKey=" + smsConfig.getApiKey()
                + "&messageContent=" + messageContent
                + "&recipients=" + mobileNumber
                + "&referenceID=" + referenceID;
        RestTemplate restTemplate = new RestTemplate();
        String url =  smsConfig.getUrl() + param;
        String result = restTemplate.getForObject(url,String.class);
        log.info("sms result:" + result);
        JSONObject resultObj = JSON.parseObject(result);
        String status = (String)resultObj.get("status");
        if(StringUtils.equals("ok",status)) {
            redisService.set(SMSKey.smsKey,mobileNumber,String.valueOf(OTPNumber));
        } else {
            throw new GlobalException(CodeMsg.SMS_CODE_SEND_ERROR);
        }
        return status;
    }


}
