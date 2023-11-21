package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsBasketballService;
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
    public Result<List<BasketballMatchVo>> getList(@RequestParam(required = false) String competitionName,
                                                 @RequestParam(required = false) String teamName,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<BasketballMatchVo> result = allSportsBasketballService.getBasketBallMatchList(competitionName,teamName,request);
        return Result.success(result);
    }
    @GetMapping("/list-start")
    public Result<Map<String, List<BasketballMatchVo>>> getAllMatchesStarts(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                            @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesStarts(request);
        return Result.success(results);
    }

    /**
     * get all past matches seven days ago
     *
     */

    @GetMapping("/list-past")
    public Result<Map<String,List<BasketballMatchVo>>> getAllMatchesPasts( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesPasts(request);
        return Result.success(results);
    }


    /**
     * get all future matches in seven days
     *
     */

    @GetMapping("/list-future")
    public Result<Map<String,List<BasketballMatchVo>>> getFootballMatchesFuture( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                               @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = allSportsBasketballService.getBasketballMatchesFuture(request);
        return Result.success(results);
    }







    /**
     * get all matches via the date
     *
     */
    @GetMapping("/list/{date}")
    public Result<List<BasketballMatchVo>> getMatchesByDate(@PathVariable("date")String date,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @RequestParam(required = false) String checkData){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<BasketballMatchVo> basketballMatchVos = allSportsBasketballService.getMatchListByDate(date,request,checkData);
        return Result.success(basketballMatchVos);
    }

    @GetMapping("/line-up/{matchId}")
    public Result<BasketballMatchLineUpVo> getMatchLineUpByMatchId(@PathVariable("matchId")String matchId){
        BasketballMatchLineUpVo basketballMatchLineUpVo = allSportsBasketballService.getBasketballMatchLineUpByMatchId(Long.parseLong(matchId));
        return Result.success(basketballMatchLineUpVo);
    }

}
