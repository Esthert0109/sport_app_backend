package com.maindark.livestream.controller;


import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FootBallService;
import com.maindark.livestream.vo.FootballLiveAddressVo;
import com.maindark.livestream.vo.FootballMatchLineUpVo;
import com.maindark.livestream.vo.FootballMatchLiveDataVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Result<List<FootballMatchVo>> getList(@RequestParam(required = false) String competitionName,
                                                 @RequestParam(required = false) String teamName,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        List<FootballMatchVo> result = footBallService.getFootBallMatchList(competitionName,teamName, request);
        return Result.success(result);
    }



    /**
    * get all matches in seven days
    * */
    @GetMapping("/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatches( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = footBallService.getFootballMatchesInSevenDays(request);
        return Result.success(results);
    }

    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<FootballMatchVo>> getMatchesByDate(@PathVariable("date")String date,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        List<FootballMatchVo> footballMatchVos = footBallService.getMatchListByDate(date,request);
        return Result.success(footballMatchVos);
    }


    /**
     * get match line-up
     *
     */
    @GetMapping("/line-up/{matchId}")
    public Result<FootballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")String matchId){
        FootballMatchLineUpVo footballMatchLineUpVo = footBallService.getFootballMatchLineUpByMatchId(Integer.parseInt(matchId));
        return Result.success(footballMatchLineUpVo);
    }

    /**
     * get match live data
     *
     */
    @GetMapping("/livedata/{matchId}")
    public Result<FootballMatchLiveDataVo> getFootballMatchLiveData(@PathVariable("matchId")String matchId){
        FootballMatchLiveDataVo footballMatchLiveData = footBallService.getMatchLiveData(Integer.parseInt(matchId));
        return Result.success(footballMatchLiveData);
    }

    /**
     * get match live address
     *
     */
    @GetMapping("/address/{matchId}")
    public Result<FootballLiveAddressVo> getLiveAddress(@PathVariable("matchId")Integer matchId){
        FootballLiveAddressVo footballLiveAddressVo = footBallService.getFootballLiveAddressByMatchId(matchId);
        return Result.success(footballLiveAddressVo);
    }

    /**
     *  test match live-up api
     *
     */
    @GetMapping("/line-up")
    public Result<Map<String,Object>> getMatchLineUp(@RequestParam("matchId")Integer matchId){
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


    /**
     *  test get data live address from nami
     *
     */




}
