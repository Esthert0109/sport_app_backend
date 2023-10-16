package com.maindark.livestream.controller;

import com.maindark.livestream.enums.SMSEnum;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.SMSValidateForm;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.SMSKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.sms.SMSConfig;
import com.maindark.livestream.sms.SMSService;
import com.maindark.livestream.vo.SMSValidateVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SMSController {

    @Resource
    SMSService smsService;
    @Resource
    RedisService redisService;

    @Resource
    SMSConfig smsConfig;

    @GetMapping ("/send/{type}/{mobile}")
    public Result<Boolean> sendMsg(@PathVariable("type")String type,@PathVariable String mobile){
        // check message limit
        Integer maxCount = redisService.get(SMSKey.smsLimit,mobile,Integer.class);
        if(maxCount != null) {
            if(maxCount > smsConfig.getMaxCount()) {
                throw new GlobalException(CodeMsg.SMS_CODE_LIMIT);
            }
        }
        Boolean result = smsService.sendSMS(type,mobile);
        return Result.success(result);
    }

    /**
     *  type 1 register send msgcode
     *  type 2 reset password msgcode
     *  type 3 forgot password
     */
    @PostMapping("/verify/mobile/{type}")
    public Result<Boolean> validateCode(@PathVariable("type")String type,@RequestBody SMSValidateForm validateForm){
        Boolean result = smsService.verifyCode(type,validateForm);
        return Result.success(result);
    }

}
