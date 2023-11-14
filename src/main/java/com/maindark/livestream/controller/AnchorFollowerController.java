package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.AnchorFollowerForm;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AnchorFollowerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/followers")
public class AnchorFollowerController {

    @Resource
    AnchorFollowerService anchorFollowerService;

    @PostMapping("/")
    public Result<Boolean> createFollow(LiveStreamUser liveStreamUser, @RequestBody @Valid AnchorFollowerForm anchorFollowerForm){
        if(liveStreamUser == null) throw new GlobalException(CodeMsg.LOGIN_IN);
        anchorFollowerService.createData(anchorFollowerForm);
        return Result.success(true);
    }

    @DeleteMapping("/{anchorId}/{followerId}")
    public Result<Boolean> deleteFollow(LiveStreamUser liveStreamUser,@PathVariable("anchorId")String anchorId,@PathVariable("followerId")String followerId){
        if(liveStreamUser == null) throw new GlobalException(CodeMsg.LOGIN_IN);
        anchorFollowerService.deleteData(Long.parseLong(anchorId),Long.parseLong(followerId));
        return Result.success(true);
    }


}
