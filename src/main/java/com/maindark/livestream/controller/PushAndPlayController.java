package com.maindark.livestream.controller;

import com.maindark.livestream.form.LiveStreamDetailForm;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.PushAndPlayService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/pushAndPlay")
public class PushAndPlayController {

    @Resource
    PushAndPlayService pushAndPlayService;

    @GetMapping("/pushUrl")
    public Result<Map<String,Object>> getPushUrl(){
        Map<String,Object> map = pushAndPlayService.getPushUrl();
        return Result.success(map) ;
    }

    @PostMapping("/create/{userId}")
    public Result<Boolean> createLiveStreamDetail(@PathVariable("userId")String userId, @Valid @RequestBody LiveStreamDetailForm liveStreamDetailForm){
        Boolean result = pushAndPlayService.createLiveStream(userId,liveStreamDetailForm);
        return Result.success(result);
    }


}
