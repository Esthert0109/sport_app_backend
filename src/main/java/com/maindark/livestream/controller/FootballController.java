package com.maindark.livestream.controller;


import com.maindark.livestream.domain.FootballMatchLiveData;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FootBallService;
import com.maindark.livestream.vo.FootballMatchLineUpVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/footballs/match")
public class FootballController {


    @Resource
    FootBallService footBallService;

    /**
    * get today's all matches via competition's name or team's name
    * */
    @GetMapping("/now-list")
    public Result<List<FootballMatchVo>> getList(@RequestParam(required = false) String competitionName, @RequestParam(required = false) String teamName){
        List<FootballMatchVo> result = footBallService.getFootBallMatchList(competitionName,teamName);
        return Result.success(result);
    }



    /**
    * get all matches in seven days
    * */
    @GetMapping("/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatches(){
        Map<String,List<FootballMatchVo>> results = footBallService.getFootballMatchesInSevenDays();
        return Result.success(results);
    }

    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<FootballMatchVo>> getMatchesByDate(@PathVariable("date")String date){
        List<FootballMatchVo> footballMatchVos = footBallService.getMatchListByDate(date);
        return Result.success(footballMatchVos);
    }


    /**
     * get match line-up
     *
     */
    @GetMapping("/line-up/{matchId}")
    public Result<FootballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")Integer matchId){
        FootballMatchLineUpVo footballMatchLineUpVo = footBallService.getFootballMatchLineUpByMatchId(matchId);
        return Result.success(footballMatchLineUpVo);
    }

    /**
     * get match live data
     *
     */
    @GetMapping("/livedata/{matchId}")
    public Result<FootballMatchLiveData> getFootballMatchLiveData(@PathVariable("matchId")Integer matchId){
        FootballMatchLiveData footballMatchLiveData = footBallService.getMatchLiveData(matchId);
        return Result.success(footballMatchLiveData);
    }

    /**
     *  test match live-up api
     *
     */
    @GetMapping("/line-up")
    public Result<Map<String,Object>> getMatchLineUp(@PathVariable("matchId")Integer matchId){
        Map<String,Object> map = footBallService.getMatchLineUp(matchId);
        return Result.success(map);
    }


    /**
     * test match live api
     *
     */
    @GetMapping("/details")
    public Result<Map<String,Object>> getMatchDetails(@RequestParam("matchId")Integer matchId){
        Map<String,Object> map = footBallService.getMatchLive(matchId);
        return Result.success(map);
    }


    /**
     *  test get data from nami via the date
     *
     */
    @GetMapping("/list/date")
    public Result<List<Map<String,Object>>> getList(@RequestParam(required = false) String competitionDate){
        List<Map<String,Object>> result = footBallService.getMatchList(competitionDate);
        return Result.success(result);
    }
}
