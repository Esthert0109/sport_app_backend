package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.GameAdService;
import com.maindark.livestream.vo.GameAdVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ads")
public class GameAdController {

    @Resource
    GameAdService gameAdService;

    @GetMapping("/url")
    public Result<GameAdVo> getGameAd(){
        GameAdVo gameAdVo = gameAdService.getGameVo();
        return Result.success(gameAdVo);
    }
}
