package com.maindark.livestream.controller;

import com.maindark.livestream.form.LiveStreamDetailForm;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.PushAndPlayService;
import com.maindark.livestream.vo.LiveStreamDetailVo;
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
    public Result<Integer> createLiveStreamDetail(@PathVariable("userId")String userId, @Valid @RequestBody LiveStreamDetailForm liveStreamDetailForm){
        Integer result = pushAndPlayService.createLiveStream(userId,liveStreamDetailForm);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<LiveStreamDetailVo> getLiveStreamDetailById(@PathVariable("id")Integer id){
        LiveStreamDetailVo liveStreamDetailVo = pushAndPlayService.getLiveStreamDetailById(id);
        return Result.success(liveStreamDetailVo);
    }

    @PatchMapping("/{id}")
    public Result<Boolean> updateLiveStreamDetailById(@PathVariable("id")Integer id,@Valid @RequestBody LiveStreamDetailForm liveStreamDetailForm){
        pushAndPlayService.updateLiveStreamDetailById(id,liveStreamDetailForm);
        return Result.success(true);
    }




}
