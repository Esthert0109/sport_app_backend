package com.maindark.livestream.controller;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.BasketBallService;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/basketballs/match")
public class BasketBallController {


    @Resource
    BasketBallService basketBallService;


    /**
     * get today's all matches via competition's name or team's name
     * */
    @GetMapping("/now-list")
    public Result<List<BasketballMatchVo>> getList(@RequestParam(required = false) String competitionName,
                                                   @RequestParam(required = false) String teamName,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        List<BasketballMatchVo> result = basketBallService.getBasketballMatchList(competitionName,teamName, request);
        return Result.success(result);
    }


    /**
     * get all today's start matches
     *
     */

    @GetMapping("/list-start")
    public Result<Map<String,List<BasketballMatchVo>>> getAllMatchesStarts( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                          @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = basketBallService.getBasketballMatchesStarts(request);
        return Result.success(results);
    }


    @GetMapping("/list-past")
    public Result<Map<String,List<BasketballMatchVo>>> getAllMatchesPasts( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        Map<String,List<BasketballMatchVo>> results = basketBallService.getBasketballMatchesPasts(request);
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
        Map<String,List<BasketballMatchVo>> results = basketBallService.getBasketballMatchesFuture(request);
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
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_time");
        List<BasketballMatchVo> footballMatchVos = basketBallService.getMatchListByDate(date,request,checkData);
        return Result.success(footballMatchVos);
    }


}
