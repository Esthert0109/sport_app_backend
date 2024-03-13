package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.AnchorFollowForm;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FollowService;
import com.maindark.livestream.service.LiveStreamUserService;
import com.maindark.livestream.vo.AnchorFollowVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping(value = "/following")
    public Result<List<AnchorFollowVo>> getFollowingListByFollowerId(LiveStreamUser liveStreamUser){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }

        List<AnchorFollowVo> followingList= followService.getFollowingListByFollowerId(liveStreamUser.getId());

        return Result.success(followingList);

    }


}
