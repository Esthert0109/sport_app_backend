package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.NamiBasketballDataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nami/basketballs")
public class NamiBasketballDataController {

    @Resource
    NamiBasketballDataService namiBasketballDataService;

    @GetMapping("/{date}")
    public Result<Boolean> getNamiData(@PathVariable("date")String date){
        namiBasketballDataService.getBasketBallDataByDate(date);
        return Result.success(true);
    }

}
