package com.maindark.livestream.controller;

import com.maindark.livestream.service.PopularGamesService;
import com.maindark.livestream.vo.PopularGamesVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/popular-games")
public class PopularGamesController {

    @Resource
    PopularGamesService popularGamesService;

    @GetMapping("/getAll")
    public List<PopularGamesVo> getAllPopularGame(){
        return popularGamesService.getAllPopularGames();
    }


}
