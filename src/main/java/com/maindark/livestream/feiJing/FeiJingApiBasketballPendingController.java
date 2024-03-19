package com.maindark.livestream.feiJing;

import com.maindark.livestream.dao.FeiJingBasketballPendingMatchDao;
import com.maindark.livestream.domain.feijing.FeijingBasketballPendingMatch;
import com.maindark.livestream.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/FeiJing/basketball")
public class FeiJingApiBasketballPendingController {
    @Resource
    FeiJingApiBasketballPendingService feijingApiBasketballService;

    @Resource
    FeiJingBasketballPendingMatchDao feijingBasketballPendingMatchDao;


    //Get All Matches to Insert Database
    @GetMapping("/matches")
    public Result<Boolean> getAllMatches(){
        feijingApiBasketballService.getAllMatches();
        return Result.success(true);
    }

    //Get All Upcoming Matches
    @GetMapping("/matches/pending")
    public Result<List<FeijingBasketballPendingMatch>> getPendingMatches() {
        List<FeijingBasketballPendingMatch> pendingMatches = feijingBasketballPendingMatchDao.getMatchesByState();
        return Result.success(pendingMatches);
    }



    @GetMapping("/matches/pending/{id}")
    public Result<FeijingBasketballPendingMatch> getUpcomingMatchesById(@PathVariable("id") Integer id){
        FeijingBasketballPendingMatch match = feijingBasketballPendingMatchDao.getMatchByStateId(id);
        return Result.success(match);
    }

}
