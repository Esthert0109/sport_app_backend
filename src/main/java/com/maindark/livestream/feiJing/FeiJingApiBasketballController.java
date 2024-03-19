package com.maindark.livestream.feiJing;

import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import com.maindark.livestream.result.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/FeiJing/basketball")
public class FeiJingApiBasketballController {
    @Resource
    FeiJingApiBasketballService feijingApiBasketballService;

    @Resource
    FeiJingBasketballMatchDao feijingBasketballMatchDao;


    //Get All Matches to Insert Database
    @GetMapping("/matches")
    public Result<Boolean> getAllMatches(){
        feijingApiBasketballService.getAllMatches();
        return Result.success(true);
    }

    //Get All Upcoming Matches
    @GetMapping("/matches/pending")
    public Result<List<FeijingBasketballMatch>> getPendingMatches() {
        List<FeijingBasketballMatch> pendingMatches = feijingBasketballMatchDao.getMatchesByState();
        return Result.success(pendingMatches);
    }


    //Get Match by State Id
    /*
     * 比赛状态码：
     * 0：未开赛
     * 1：一节
     * 2：二节
     * 3：三节
     * 4：四节
     * 5：1'OT (第一加时赛)
     * 6：2'OT (第二加时赛)
     * 7：3'OT (第三加时赛)
     * 50：中场
     * -1：完场
     * -2：待定
     * -3：中断
     * -4：取消
     * -5：推迟
     */
    @GetMapping("/matches/pending/{stateId}")
    public Result<FeijingBasketballMatch> getUpcomingMatchesById(@PathVariable("stateId") Integer stateId){
        FeijingBasketballMatch match = feijingBasketballMatchDao.getMatchByStateId(stateId);
        return Result.success(match);
    }

}
