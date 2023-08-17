package com.maindark.livestream.sms;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.util.UUIDUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class SMSService {

    @Resource
    SMSConfig smsConfig;
    public  String sendSMS(String mobileNumber) {
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

        // GET REQUEST TO Server
//        String result = HttpUtils.sendGet(Constants.SMS_URL_HEADER, param);
//        JSONObject resultObj = JSON.parseObject(result);
        return null;
    }


}
