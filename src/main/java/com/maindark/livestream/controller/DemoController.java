package com.maindark.livestream.controller;


import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.UserKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LiveStreamUserService;
import com.maindark.livestream.sms.SMSService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {
    @Resource
    LiveStreamUserService liveStreamUserService;
    @Resource
    RedisService redisService;

    @Resource
    SMSService smsService;
    @GetMapping("/hello")
    public Result<String>  hell(){
        return Result.success("hello World");
    }

    @GetMapping("/helloWorld")
    public Result<String> error(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @GetMapping("/db/get")
    public Result<LiveStreamUser> dbGet(@RequestParam String id){
        LiveStreamUser user = liveStreamUserService.getById(Long.valueOf(id));
        return Result.success(user);
    }

    @GetMapping("/redis/get")
    public Result<LiveStreamUser> getDataFromRedis(){
       LiveStreamUser value = redisService.get(UserKey.getById,"3",LiveStreamUser.class);
       return Result.success(value);
    }

    @GetMapping("/redis/set")
    public Result<String> setRedis(){

            LiveStreamUser liveStreamUser = new LiveStreamUser();
            liveStreamUser.setId(Long.valueOf(123));
            liveStreamUser.setPassword("123459");
            liveStreamUser.setNickName("hongFeiDu");
            liveStreamUser.setLoginCount(1);
            Boolean bool =  redisService.set(UserKey.getById,""+3,liveStreamUser);

//           String key2 = redisService.get(UserKey.getById,String.class);
//           return Result.success(key2);
        return null;
    }

    @GetMapping("/sms/send")
    public Result<Boolean> sendSMS(String mobile) throws Exception{
        log.info(mobile);
        Boolean result =smsService.sendSMS(mobile);
        return Result.success(result);
    }
}
