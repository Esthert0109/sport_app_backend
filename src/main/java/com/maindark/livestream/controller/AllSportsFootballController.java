package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsService;
import com.maindark.livestream.vo.AllSportsFootballMatchLineUpVo;
import com.maindark.livestream.vo.AllSportsFootballMatchLiveDataVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/footballs/match/en")
public class AllSportsFootballController {
    @Resource
    AllSportsService allSportsService;
    /**
     * get today's all matches via competition's name or team's name
     * */
    @GetMapping("/now-list")
    public Result<List<FootballMatchVo>> getList(@RequestParam(required = false) String competitionName,
                                                 @RequestParam(required = false) String teamName,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<FootballMatchVo> result = allSportsService.getFootBallMatchList(competitionName,teamName,request);
        return Result.success(result);
    }



    /**
     * get all matches in seven days
     * */
    @GetMapping("/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatches( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesInSevenDays(request);
        return Result.success(results);
    }

    /**
     * get all today's start matches
     *
     */

    @GetMapping("/list-start")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatchesStarts( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                          @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesStarts(request);
        return Result.success(results);
    }

    /**
     * get all past matches seven days ago
     *
     */

    @GetMapping("/list-past")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatchesPasts( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesPasts(request);
        return Result.success(results);
    }


    /**
     * get all future matches in seven days
     *
     */

    @GetMapping("/list-future")
    public Result<Map<String,List<FootballMatchVo>>> getFootballMatchesFuture( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                               @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesFuture(request);
        return Result.success(results);
    }







    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<FootballMatchVo>> getMatchesByDate(@PathVariable("date")String date,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) String checkData){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<FootballMatchVo> footballMatchVos = allSportsService.getMatchListByDate(date,request,checkData);
        return Result.success(footballMatchVos);
    }


    /**
     * get match line-up
     *
     */
    @GetMapping("/line-up/{matchId}")
    public Result<AllSportsFootballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")String matchId){
        AllSportsFootballMatchLineUpVo footballMatchLineUpVo = allSportsService.getFootballMatchLineUpByMatchId(matchId);
        return Result.success(footballMatchLineUpVo);
    }

    /**
     * get match live data
     *
     */
    @GetMapping("/livedata/{matchId}")
    public Result<AllSportsFootballMatchLiveDataVo> getFootballMatchLiveData(@PathVariable("matchId")String matchId){
        AllSportsFootballMatchLiveDataVo footballMatchLiveData = allSportsService.getMatchLiveData(Long.parseLong(matchId));
        return Result.success(footballMatchLiveData);
    }

    @GetMapping("/address/{teamName}")
    public Result<String> getLiveAddress(@PathVariable("teamName")String teamName){
        String address = allSportsService.getLiveAddress(teamName);
        return Result.success(address);
    }


}
