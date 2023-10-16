package com.maindark.livestream.allSports;


import com.maindark.livestream.result.Result;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/allSports")
public class AllSportsApiFootballController {

    @Resource
    AllSportsApiService allSportsApiService;
    @GetMapping("/leagues/list")
    public Result<Map<String,Object>> getAllLeagues(){
        Map<String,Object> map = allSportsApiService.getAllLeagues();
        return Result.success(map);
    }

    @GetMapping("/teams/list/{leagueId}")
    public Result<Map<String,Object>> getAllTeams(@PathVariable("leagueId")String leagueId){
        Map<String,Object> map = allSportsApiService.getAllTeams(leagueId);
        return Result.success(map);
    }

    @GetMapping("/fixtures/list")
    public Result<Map<String,Object>> getAllFixtures(@RequestParam("from")String from,@RequestParam("to")String to){
        Map<String,Object> map = allSportsApiService.getAllFixtures(from,to);
        return Result.success(map);
    }

    @GetMapping("/fixtures/{matchId}")
    public Result<Map<String,Object>> getAllLiveMatch(@PathVariable("matchId") String matchId){
        Map<String,Object> map = allSportsApiService.getLiveMatchByMatchId(matchId);
        return Result.success(map);
    }
}
