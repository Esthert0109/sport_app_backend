package com.maindark.livestream.allSports;


import com.maindark.livestream.domain.AllSportsFootballMatch;
import com.maindark.livestream.result.Result;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public Result<List<AllSportsFootballMatch>> getAllFixtures(@RequestParam("from")String from, @RequestParam("to")String to){
        List<AllSportsFootballMatch> list = allSportsApiService.getAllFixtures(from,to);
        return Result.success(list);
    }

    /**
     * get match line up from fixtures api
     *
     */

    @GetMapping("/fixtures/{matchId}")
    public Result<Map<String,Object>> getAllLiveMatch(@PathVariable("matchId") String matchId){
        Map<String,Object> map = allSportsApiService.getLiveMatchByMatchId(matchId);
        return Result.success(map);
    }

    @GetMapping("/liveScore/{matchId}")
    public Result<Map<String,Object>> getLiveMatchAndLineUpData(@PathVariable("matchId") String matchId){
        Map<String,Object> map = allSportsApiService.getLiveMatchViaLiveDataApi(matchId);
        return Result.success(map);
    }

    @GetMapping("/fixtures/livedata/{matchId}")
    public Result<Map<String,Object>> getLiveData(@PathVariable("matchId") String matchId){
        Map<String,Object> map = allSportsApiService.getLiveData(matchId);
        return Result.success(map);
    }
}
