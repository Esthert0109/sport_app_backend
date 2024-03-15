package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.dao.FeiJingBasketballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballMatch;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@EnableScheduling
public class FeiJingBasketballScoreTask {

    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingBasketballMatchDao feiJingBasketballMatchDao;


    @Scheduled(cron = "*/20 * * * * *")
    public void changeBasketballScore(){
        String url = feiJingConfig.getBasketballChangeMatch();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> changeList = (List<Map<String,Object>>) resultObj.get("changeList");
        if(changeList != null && !changeList.isEmpty()) {
            changeList.forEach(match ->{
                Integer matchId = (Integer) match.get("matchId");
                int existed = feiJingBasketballMatchDao.queryExisted(matchId);
                if(existed >0) {
                    FeiJingBasketballMatch feiJingBasketballMatch = new FeiJingBasketballMatch();
                    Integer state = (Integer)match.get("matchState");
                    Integer homeScore = (Integer)match.get("homeScore");
                    Integer awayScore = (Integer)match.get("awayScore");
                    Integer home1 = (Integer) match.get("home1");
                    Integer home2 = (Integer) match.get("home2");
                    Integer home3 = (Integer) match.get("home3");
                    Integer home4 = (Integer) match.get("home4");
                    Integer away1 = (Integer) match.get("away1");
                    Integer away2 = (Integer) match.get("away2");
                    Integer away3 = (Integer) match.get("away3");
                    Integer away4 = (Integer) match.get("away4");
                    Boolean hasStats = (Boolean) match.get("hasStats");
                    feiJingBasketballMatch.setHomeScore(homeScore);
                    feiJingBasketballMatch.setHFQuarter(home1);
                    feiJingBasketballMatch.setHTQuarter(home2);
                    feiJingBasketballMatch.setHTQuarter(home3);
                    feiJingBasketballMatch.setH4Quarter(home4);
                    feiJingBasketballMatch.setAwayScore(awayScore);
                    feiJingBasketballMatch.setAFQuarter(away1);
                    feiJingBasketballMatch.setASQuarter(away2);
                    feiJingBasketballMatch.setATQuarter(away3);
                    feiJingBasketballMatch.setA4Quarter(away4);
                    feiJingBasketballMatch.setStatus(state.toString());
                    feiJingBasketballMatch.setHasState(hasStats);
                    feiJingBasketballMatchDao.updateMatchScoreByMatchId(feiJingBasketballMatch);
                }
            });
        }



    }
}
