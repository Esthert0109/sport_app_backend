package com.maindark.livestream.controller;


import com.alibaba.fastjson.JSON;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FootBallService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/footballs")
public class FootballController {


    @Resource
    FootBallService footBallService;

    @GetMapping("/list")
    public Result<Object> getList(@RequestParam String competitionDate){
        String result = footBallService.getFootBallMatchList(competitionDate);
        Object object = JSON.parseObject(result);
        return Result.success(object);
    }
}
