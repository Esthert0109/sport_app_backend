package com.maindark.livestream.controller;


import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FootBallService;
import com.maindark.livestream.vo.FootballMatchVo;
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

    @GetMapping("/match/list")
    public Result<List<FootballMatchVo>> getList(@RequestParam(required = false) String competitionName, @RequestParam(required = false) String teamName){
        List<FootballMatchVo> result = footBallService.getFootBallMatchList(competitionName,teamName);
        return Result.success(result);
    }
}
