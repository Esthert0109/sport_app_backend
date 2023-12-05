package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.PushAndPlayService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
