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
import org.springframework.web.bind.annotation.PathVariable;
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
        LiveStreamUser user = liveStreamUserService.getById(Long.parseLong(id));
        return Result.success(user);
    }

    @GetMapping("/redis/get/{redisKey}")
    public Result<LiveStreamUser> getDataFromRedis(@PathVariable("redisKey")String redisKey){
       LiveStreamUser value = redisService.get(UserKey.getById,redisKey,LiveStreamUser.class);
       return Result.success(value);
    }

    @GetMapping("/redis/set/{redisKey}")
    public Result<String> setRedis(@PathVariable("redisKey")String redisKey){

            LiveStreamUser liveStreamUser = new LiveStreamUser();
            liveStreamUser.setId(Long.parseLong("123455566778"));
            liveStreamUser.setPassword("test1234556");
            liveStreamUser.setNickName("hongFeiDu");
            liveStreamUser.setLoginCount(1);
            Boolean bool =  redisService.set(UserKey.getById,redisKey,liveStreamUser);

//           String key2 = redisService.get(UserKey.getById,String.class);
//           return Result.success(key2);
        return null;
    }

    @GetMapping("/sms/send")
    public Result<Boolean> sendSMS(String mobile){
        log.info(mobile);
        Boolean result =smsService.sendSMS(mobile);
        return Result.success(result);
    }
}
