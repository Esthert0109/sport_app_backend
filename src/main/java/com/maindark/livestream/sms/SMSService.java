package com.maindark.livestream.sms;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.dao.LiveStreamUserDao;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.enums.SMSEnum;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.SMSValidateForm;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.SMSKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.UUIDUtil;
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
    LiveStreamUserDao liveStreamUserDao;

    @Resource
    RedisService redisService;
    public  Boolean sendSMS(String type,String mobileNumber)  {
        // check the mobile is existed when reset the password or forgot the password.
        if(StringUtils.equals(type,SMSEnum.RESET.getCode()) || StringUtils.equals(type,SMSEnum.FORGOT.getCode())){
            LiveStreamUser liveStreamUser = liveStreamUserDao.getById(Long.parseLong(mobileNumber));
            if(liveStreamUser == null){
                throw  new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
            }
        }

        // check the mobile is existed when register
        if(StringUtils.equals(type,SMSEnum.REGISTER.getCode())){
            LiveStreamUser liveStreamUser = liveStreamUserDao.getById(Long.parseLong(mobileNumber));
            if(liveStreamUser != null){
                throw  new GlobalException(CodeMsg.MOBILE_EXIST);
            }
        }

        // 随机的四位数OTP
        Random random = new Random();
        int OTPNumber = random.nextInt(9000) + 1000;
        String messageContent = smsConfig.getContent() + OTPNumber;
        if(mobileNumber.startsWith("86")){
            messageContent = smsConfig.getChinaContent().replace("%%",OTPNumber+"");
        }
        //String messageContent = smsConfig.getContent() + OTPNumber;
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
        log.info("receive msg result:{}",result);
        JSONObject resultObj = JSON.parseObject(result);
        String status = (String)resultObj.get("status");
        if(StringUtils.equals("ok",status)) {
            Integer count = redisService.get(SMSKey.smsLimit,mobileNumber,Integer.class);
            if(count != null) {
                redisService.incr(SMSKey.smsLimit,mobileNumber,0);
            } else {
                redisService.set(SMSKey.smsLimit,mobileNumber,0);
            }
            if(StringUtils.equals(type, SMSEnum.REGISTER.getCode())){
                redisService.set(SMSKey.smsKey,mobileNumber,String.valueOf(OTPNumber));
            } else if (StringUtils.equals(type,SMSEnum.RESET.getCode())) {
                redisService.set(SMSKey.resetPass,mobileNumber,String.valueOf(OTPNumber));
            } else if (StringUtils.equals(SMSEnum.FORGOT.getCode(),type)){
                redisService.set(SMSKey.forgotPass,mobileNumber,String.valueOf(OTPNumber));
            }
            return true;
        } else {
            log.error("send msg url:{}",url);
            log.error("send msg result:{}",result);
            throw new GlobalException(CodeMsg.SMS_CODE_SEND_ERROR);
        }
    }

    public Boolean verifyCode(String type,SMSValidateForm smsValidateForm) {
        String mobile = smsValidateForm.getMobile();
        String code = smsValidateForm.getCode();
        String redisCode = "";
        if(StringUtils.equals(type, SMSEnum.REGISTER.getCode())){
            redisCode = redisService.get(SMSKey.smsKey,mobile,String.class);
        } else if (StringUtils.equals(type,SMSEnum.RESET.getCode())) {
            redisCode = redisService.get(SMSKey.resetPass,mobile,String.class);
        } else if (StringUtils.equals(type,SMSEnum.FORGOT.getCode())){
            redisCode = redisService.get(SMSKey.forgotPass,mobile,String.class);
        }

        if(!StringUtils.isBlank(redisCode)){
            if(StringUtils.equals(code,redisCode)) {
                if(StringUtils.equals(type, SMSEnum.REGISTER.getCode())){
                    redisService.delete(SMSKey.smsKey,mobile);
                } else if (StringUtils.equals(type,SMSEnum.RESET.getCode())) {
                    redisService.delete(SMSKey.resetPass,mobile);
                } else if (StringUtils.equals(type,SMSEnum.FORGOT.getCode())){
                    redisService.delete(SMSKey.forgotPass,mobile);
                }
                return true;
            } else {
                throw  new GlobalException(CodeMsg.SMS_CODE_ERROR);
            }
        } else {
            throw  new GlobalException(CodeMsg.SMS_CODE_NOT_EXIST);
        }
    }
}
