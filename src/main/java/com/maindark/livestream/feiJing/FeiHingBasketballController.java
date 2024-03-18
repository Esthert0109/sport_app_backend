package com.maindark.livestream.feiJing;

import com.maindark.livestream.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/feiHing/basketball")
public class FeiHingBasketballController {

    @Resource
    FeiHingApiBasketballService feiHingApiBasketballService;

    @GetMapping("/teams")
    public Result<Boolean> getAllTeams(){
        feiHingApiBasketballService.getAllTeams();
        return Result.success(true);
    }

    @GetMapping("/match/{matchDate}")
    public Result<List<Map<String,Object>>> getMatches(@PathVariable("matchDate")String matchDate){
        List<Map<String,Object>> res = feiHingApiBasketballService.getMatchesByDate(matchDate);
        return Result.success(res);
    }



}
