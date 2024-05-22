package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.PopularGamesService;
import com.maindark.livestream.vo.PopularGamesVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/popular-games")
@CrossOrigin
public class PopularGamesController {

    @Resource
    PopularGamesService popularGamesService;

    @GetMapping("/getAll")
    public Result<List<PopularGamesVo>> getAllPopularGame(){
        return Result.success(popularGamesService.getAllPopularGames());
    }


}
