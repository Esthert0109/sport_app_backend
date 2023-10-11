package com.maindark.livestream.sms;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.SMSKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.UUIDUtil;
import com.maindark.livestream.vo.SMSValidateVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
@Slf4j
public class SMSService {

    @Resource
    SMSConfig smsConfig;

    @Resource
    RedisService redisService;
    public  Boolean sendSMS(String mobileNumber)  {
        // 随机的四位数OTP
        Random random = new Random();
        int OTPNumber = random.nextInt(9000) + 1000;
        String messageContent = smsConfig.getContent() + OTPNumber;
        // 自动生成一个 referenceID
        String referenceID = UUIDUtil.uuid();
        String param = "apiKey=" + smsConfig.getApiKey()
                + "&messageContent=" + messageContent
                + "&recipients=" + mobileNumber
                + "&referenceID=" + referenceID;
        RestTemplate restTemplate = new RestTemplate();
        String url =  smsConfig.getUrl() + param;
        url = URLDecoder.decode(url, Charset.forName(StandardCharsets.UTF_8.name()));
        String result = restTemplate.getForObject(url, String.class);
        log.info("sms result:{}",result);
        JSONObject resultObj = JSON.parseObject(result);
        String status = (String)resultObj.get("status");
        Integer maxCount = redisService.get(SMSKey.smsLimit,mobileNumber,Integer.class);
        // check message limit
        if(maxCount != null) {
            if(maxCount > 10) {
                throw new GlobalException(CodeMsg.SMS_CODE_SEND_ERROR);
            }
        }

        if(StringUtils.equals("ok",status)) {
            redisService.incr(SMSKey.smsLimit,mobileNumber,0);
            redisService.set(SMSKey.smsKey,mobileNumber,String.valueOf(OTPNumber));
            return true;
        } else {
            log.error("send msg url:{}",url);
            log.error("send smg result:{}",result);
            throw new GlobalException(CodeMsg.SMS_CODE_SEND_ERROR);
        }
    }

    public Boolean verifyCode(SMSValidateVo smsValidateVo) {
        String mobile = smsValidateVo.getMobile();
        String code = smsValidateVo.getCode();
        String redisCode = redisService.get(SMSKey.smsKey,mobile,String.class);
        if(!StringUtils.isBlank(redisCode)){
            if(StringUtils.equals(code,redisCode)) {
                return true;
            } else {
                throw  new GlobalException(CodeMsg.SMS_CODE_ERROR);
            }
        } else {
            throw  new GlobalException(CodeMsg.SMS_CODE_NOT_EXIST);
        }
    }
}
