package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.ApplyForLiveService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applies")
@CrossOrigin
public class ApplyLiveController {

    @Resource
    ApplyForLiveService applyForLiveService;
    @PostMapping("/")
    public Result<Boolean> createApplyLive(LiveStreamUser liveStreamUser){
        if(liveStreamUser == null) throw  new GlobalException(CodeMsg.LOGIN_IN);
        Long userId = liveStreamUser.getId();
        applyForLiveService.createApplyForLive(userId);
        return Result.success(true);
    }

}
