package com.maindark.livestream.controller;

import com.maindark.livestream.service.PopularGamesService;
import com.maindark.livestream.vo.PopularGamesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/popular-games")
public class PopularGamesController {

    @Autowired
    PopularGamesService popularGamesService;


    @GetMapping("/getAll")
    public List<PopularGamesVo> getAllPopularGame(){
        return popularGamesService.getAllPopularGames();
    }


}
