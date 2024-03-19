package com.maindark.livestream.feiJing;

import com.maindark.livestream.dao.FeijingBasketballMatchDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import com.maindark.livestream.result.Result;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/FeiJing/basketball")
public class FeiJingApiBasketballController {
    @Resource
    FeiJingApiBasketballService feijingApiBasketballService;

    @Resource
    FeijingBasketballMatchDao feijingBasketballMatchDao;


//    @GetMapping("/matches/{matchDate}")
//    public Result<List<Map<String,Object>>> getBasketball(@PathVariable("matchDate")String matchDate){
//        List<Map<String,Object>> res = feijingApiBasketballService.getMatchByDate(matchDate);
//        return Result.success(res);
//    }

    //Get All Match
    @GetMapping("/matches")
    public Result<Boolean> getAllMatches(){
        feijingApiBasketballService.getAllMatches();
        return Result.success(true);
    }

    //Get Upcoming Match
    @GetMapping("/pending")
    public Result<List<Map<String, Object>>> getUpcomingMatches(){
        List<Map<String, Object>> matches = feijingApiBasketballService.getMatches();
        return Result.success(matches);
    }

    //Get Match by State Id
    @GetMapping("/pending/{stateId}")
    public Result<List<Map<String, Object>>> getUpcomingMatches(){
        List<Map<String, Object>> matches = feijingApiBasketballService.getMatches();
        return Result.success(matches);
    }
}
