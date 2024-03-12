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
@RequestMapping("/api/v1/feiHing")
public class FeiHingApiFootballController {

    @Resource
    FeiHingApiFootballService feiHingApiFootballService;

    @GetMapping("/teams")
    public Result<Boolean> getAllTeams(){
        feiHingApiFootballService.getAllTeams();
        return Result.success(true);
    }

    @GetMapping("/matches/{matchDate}")
    public Result<List<Map<String,Object>>> getFootballs(@PathVariable("matchDate")String matchDate){
        List<Map<String,Object>> res = feiHingApiFootballService.getMatchByDate(matchDate);
        return Result.success(res);
    }
}
