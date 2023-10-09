package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.sms.SMSService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SMSController {

    @Resource
    SMSService smsService;

    @GetMapping ("/send/{mobile}")
    public Result<Boolean> sendMsg(@PathVariable String mobile){
        Boolean result = smsService.sendSMS(mobile);
        return Result.success(result);
    }
}
