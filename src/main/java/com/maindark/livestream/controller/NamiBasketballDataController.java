package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.NamiBasketballDataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nami/basketballs")
@CrossOrigin
public class NamiBasketballDataController {

    @Resource
    NamiBasketballDataService namiBasketballDataService;

    @GetMapping("/{date}")
    public Result<Boolean> getNamiData(@PathVariable("date")String date){
        namiBasketballDataService.getBasketBallDataByDate(date);
        return Result.success(true);
    }

}
