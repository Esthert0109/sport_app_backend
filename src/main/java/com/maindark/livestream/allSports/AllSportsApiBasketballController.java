package com.maindark.livestream.allSports;

import com.maindark.livestream.domain.AllSportsBasketballMatch;
import com.maindark.livestream.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/allSports/basketball")
public class AllSportsApiBasketballController {

    @Resource
    AllSportsApiBasketballService allSportsApiBasketballService;
    @GetMapping("/fixtures/list")
    public Result<List<AllSportsBasketballMatch>> getAllFixtures(@RequestParam("from")String from, @RequestParam("to")String to){
        List<AllSportsBasketballMatch> list = allSportsApiBasketballService.getAllFixtures(from,to);
        return Result.success(list);
    }
}
