package com.maindark.livestream.controller;


import com.alibaba.fastjson.JSON;
import com.maindark.livestream.domain.FootballCompetition;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FootBallService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/footballs")
public class FootballController {


    @Resource
    FootBallService footBallService;

    @GetMapping("/list")
    public Result<List<Map<String,Integer>>> getList(@RequestParam String competitionDate,@RequestParam Integer id,@RequestParam Integer time){
        List<Map<String,Integer>> result = footBallService.getFootBallMatchList(competitionDate,id,time);
        return Result.success(result);
    }
}
