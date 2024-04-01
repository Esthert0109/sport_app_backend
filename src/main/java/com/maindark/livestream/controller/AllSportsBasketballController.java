package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsBasketballService;
import com.maindark.livestream.vo.AllSportsBasketballLiveDataVo;
import com.maindark.livestream.vo.BasketballMatchLineUpVo;
import com.maindark.livestream.vo.BasketballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/basketballs/match/en")
public class AllSportsBasketballController {

    @Resource
    AllSportsBasketballService allSportsBasketballService;

    /**
     * get all today's start matches
     *
     */


    @GetMapping("/now-list")
    public Result<List<BasketballMatchVo>> getList(LiveStreamUser liveStreamUser, @RequestParam("search") String search,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<BasketballMatchVo> result = allSportsBasketballService.getBasketBallMatchList(search,request,userId);
        return Result.success(result);
    }
    @GetMapping("/list-start")
    public Result<Map<String, List<BasketballMatchVo>>> getAllMatchesStarts(LiveStreamUser liveStreamUser,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                            @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesStarts(userId,request);
        return Result.success(results);
    }

    /**
     * get all past matches seven days ago
     *
     */

    @GetMapping("/list-past")
    public Result<Map<String,List<BasketballMatchVo>>> getAllMatchesPasts(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesPasts(userId,request);
        return Result.success(results);
    }


    /**
     * get all future matches in seven days
     *
     */

    @GetMapping("/list-future")
    public Result<Map<String,List<BasketballMatchVo>>> getFootballMatchesFuture(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                               @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesFuture(userId,request);
        return Result.success(results);
    }







    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<BasketballMatchVo>> getMatchesByDate(LiveStreamUser liveStreamUser,@PathVariable("date")String date,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) String checkData){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<BasketballMatchVo> basketballMatchVos = allSportsBasketballService.getMatchListByDate(userId,date,request,checkData);
        return Result.success(basketballMatchVos);
    }

    @GetMapping("/line-up/{matchId}")
    public Result<BasketballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")String matchId){
        BasketballMatchLineUpVo basketballMatchLineUpVo = allSportsBasketballService.getBasketballMatchLineUpByMatchId(Long.parseLong(matchId));
        return Result.success(basketballMatchLineUpVo);
    }

    /**
     * get match live data
     *
     */
    @GetMapping("/livedata/{matchId}")
    public Result<AllSportsBasketballLiveDataVo> getBasketballMatchLiveData(@PathVariable("matchId")String matchId){
        AllSportsBasketballLiveDataVo basketballMatchLiveDataVo = allSportsBasketballService.getMatchLiveData(Long.parseLong(matchId));
        return Result.success(basketballMatchLiveDataVo);
    }

    /**
     * get all matches in seven days
     *
     */

    @GetMapping("/list")
    public Result<Map<String,List<BasketballMatchVo>>> getAllMatches(LiveStreamUser liveStreamUser,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        Long userId = null;
        if(liveStreamUser != null) {
            userId = liveStreamUser.getId();
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesInSevenDays(userId,request);
        return Result.success(results);
    }


}
