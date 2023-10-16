package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsApiService;
import com.maindark.livestream.vo.FootballMatchLineUpVo;
import com.maindark.livestream.vo.FootballMatchLiveDataVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/footballs/match/en")
public class AllSportsController {
    @Resource
    AllSportsApiService allSportsApiService;
    /**
     * get today's all matches via competition's name or team's name
     * */
    @GetMapping("/now-list")
    public Result<List<FootballMatchVo>> getList(@RequestParam(required = false) String competitionName, @RequestParam(required = false) String teamName){
        List<FootballMatchVo> result = allSportsApiService.getFootBallMatchList(competitionName,teamName);
        return Result.success(result);
    }



    /**
     * get all matches in seven days
     * */
    @GetMapping("/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatches(){
        Map<String,List<FootballMatchVo>> results = allSportsApiService.getFootballMatchesInSevenDays();
        return Result.success(results);
    }

    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<FootballMatchVo>> getMatchesByDate(@PathVariable("date")String date){
        List<FootballMatchVo> footballMatchVos = allSportsApiService.getMatchListByDate(date);
        return Result.success(footballMatchVos);
    }


    /**
     * get match line-up
     *
     */
    @GetMapping("/line-up/{matchId}")
    public Result<FootballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")String matchId){
        FootballMatchLineUpVo footballMatchLineUpVo = allSportsApiService.getFootballMatchLineUpByMatchId(matchId);
        return Result.success(footballMatchLineUpVo);
    }

    /**
     * get match live data
     *
     */
    @GetMapping("/livedata/{matchId}")
    public Result<FootballMatchLiveDataVo> getFootballMatchLiveData(@PathVariable("matchId")String matchId){
        FootballMatchLiveDataVo footballMatchLiveData = allSportsApiService.getMatchLiveData(Integer.parseInt(matchId));
        return Result.success(footballMatchLiveData);
    }


}
