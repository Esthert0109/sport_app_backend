package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.AnchorFollowForm;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FollowService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/follow")
public class AnchorFollowController {

    @Resource
    FollowService followService;

    @PostMapping(value = "/create")
    public Result<Boolean> createFollow (@RequestBody AnchorFollowForm anchorFollowForm, LiveStreamUser liveStreamUser){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }

        followService.createFollow(anchorFollowForm.getAnchorId(), liveStreamUser.getId());

        return Result.success(true);

    }
}
