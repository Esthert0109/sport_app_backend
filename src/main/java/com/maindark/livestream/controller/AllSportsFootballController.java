package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsFootballService;
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
@CrossOrigin
public class AllSportsFootballController {
    @Resource
    AllSportsFootballService allSportsService;
    /**
     * get today's all matches via competition's name or team's name
     * */
    @GetMapping("/now-list")
    public Result<List<FootballMatchVo>> getList(LiveStreamUser liveStreamUser,@RequestParam("search") String search,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");

        List<FootballMatchVo> result = allSportsService.getFootBallMatchList(search,request,userId);
        return Result.success(result);
    }



    /**
     * get all matches in seven days
     * */
    @GetMapping("/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllMatches(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                    @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesInSevenDays(userId,request);
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
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesStarts(userId,request);
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
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesPasts(userId,request);
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
        Map<String,List<FootballMatchVo>> results = allSportsService.getFootballMatchesFuture(userId,request);
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
        List<FootballMatchVo> footballMatchVos = allSportsService.getMatchListByDate(userId,date,request,checkData);
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

    @GetMapping("/address")
    public Result<String> getLiveAddress(@RequestParam("homeTeamName")String homeTeamName,@RequestParam("awayTeamName") String awayTeamName){
        String address = allSportsService.getLiveAddress(homeTeamName,awayTeamName);
        return Result.success(address);
    }


}
