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
import org.springframework.data.domain.PageRequest;
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
    public Result<List<AnchorFollowVo>> getFollowingListByFollowerId(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "15") Integer size){

        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }

        PageRequest request = PageRequest.of(page -1, size);

        List<AnchorFollowVo> followingList= followService.getFollowingListByFollowerId(liveStreamUser.getId(), request);

        return Result.success(followingList);

    }

    @GetMapping(value = "/following/desc")
    public Result<List<AnchorFollowVo>> getFollowingListByDescOrder(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "15") Integer size){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        PageRequest request = PageRequest.of(page -1, size);

        List<AnchorFollowVo> followingList= followService.getFollowingListByDescOrder(liveStreamUser.getId(), request);

        return Result.success(followingList);

    }

    @GetMapping(value = "/following/asc")
    public Result<List<AnchorFollowVo>> getFollowingListByAscOrder(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "15") Integer size){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        PageRequest request = PageRequest.of(page -1, size);

        List<AnchorFollowVo> followingList= followService.getFollowingListByAscOrder(liveStreamUser.getId(), request);

        return Result.success(followingList);

    }


}
