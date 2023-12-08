package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsBasketballService;
import com.maindark.livestream.service.AllSportsFootballService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/live-address/")
public class AllSportsController {

    @Resource
    AllSportsFootballService allSportsFootballService;

    @Resource
    AllSportsBasketballService allSportsBasketballService;

    @GetMapping("/football")
    public Result<String> getFootballLiveAddress(@RequestParam("homeTeamName")String homeTeamName, @RequestParam("awayTeamName") String awayTeamName){
        String address = allSportsFootballService.getLiveAddress(homeTeamName,awayTeamName);
        return Result.success(address);
    }

    @GetMapping("/basketball")
    public Result<String> getBasketballLiveAddress(@RequestParam("homeTeamName")String homeTeamName, @RequestParam("awayTeamName") String awayTeamName){
        String address = allSportsBasketballService.getLiveAddress(homeTeamName,awayTeamName);
        return Result.success(address);
    }
}
