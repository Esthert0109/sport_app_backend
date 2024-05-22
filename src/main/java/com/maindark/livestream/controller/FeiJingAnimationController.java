package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FeijingAnimationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/animation")
@CrossOrigin
public class FeiJingAnimationController {

    @Resource
    FeijingAnimationService feijingAnimationService;

    @GetMapping("/{sportType}/{matchId}")
    public Result<String> getAnimationUrl(@PathVariable("sportType")String sportType,@PathVariable("matchId")String matchId){
        String url = feijingAnimationService.getAnimationUrl(sportType,matchId);
        return Result.success(url);
    }
}
