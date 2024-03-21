package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.FeiJingFootballService;
import com.maindark.livestream.vo.*;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/footballs/match/zh")
public class FeiJingFootballController {

    @Resource
    FeiJingFootballService feiJingFootballService;
    /**
     * get today's all matches via competition's name or team's name
     * */
    @GetMapping("/now-list")
    public Result<List<FootballMatchVo>> getList(LiveStreamUser liveStreamUser, @RequestParam("search") String search,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");

        List<FootballMatchVo> result = feiJingFootballService.getFootBallMatchList(search,request,userId);
        return Result.success(result);
    }



    /**
     * get all matches in seven days
     * */
    @GetMapping("/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatches(LiveStreamUser liveStreamUser,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Map<String,List<FootballMatchVo>> results = feiJingFootballService.getFootballMatchesInSevenDays(userId,request);
        return Result.success(results);
    }

    /**
     * get all today's start matches
     *
     */

    @GetMapping("/list-start")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatchesStarts(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }

        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = feiJingFootballService.getFootballMatchesStarts(userId,request);
        return Result.success(results);
    }

    /**
     * get all past matches seven days ago
     *
     */

    @GetMapping("/list-past")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatchesPasts(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                        @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }

        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = feiJingFootballService.getFootballMatchesPasts(userId,request);
        return Result.success(results);
    }


    /**
     * get all future matches in seven days
     *
     */

    @GetMapping("/list-future")
    public Result<Map<String,List<FootballMatchVo>>> getFootballMatchesFuture(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                              @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }

        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<FootballMatchVo>> results = feiJingFootballService.getFootballMatchesFuture(userId,request);
        return Result.success(results);
    }







    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<FootballMatchVo>> getMatchesByDate(LiveStreamUser liveStreamUser,@PathVariable("date")String date,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) String checkData){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }

        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<FootballMatchVo> footballMatchVos = feiJingFootballService.getMatchListByDate(userId,date,request,checkData);
        return Result.success(footballMatchVos);
    }


    /**
     * get match line-up
     *
     */
    @GetMapping("/line-up/{matchId}")
    public Result<FeiJingFootballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")String matchId){
        FeiJingFootballMatchLineUpVo footballMatchLineUpVo = feiJingFootballService.getFootballMatchLineUpByMatchId(matchId);
        return Result.success(footballMatchLineUpVo);
    }

    /**
     * get match live data
     *
     */
    @GetMapping("/livedata/{matchId}")
    public Result<FeiJingFootballMatchLiveDataVo> getFootballMatchLiveData(@PathVariable("matchId")String matchId){
        FeiJingFootballMatchLiveDataVo footballMatchLiveData = feiJingFootballService.getMatchLiveData(Integer.parseInt(matchId));
        return Result.success(footballMatchLiveData);
    }

}
