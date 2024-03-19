package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.dao.FeiJingBasketballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballMatch;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
                    String homeTeamScore = (String) match.get("homeScore");
                    String home1 = (String) match.get("home1");
                    String home2 = (String) match.get("home2");
                    String home3 = (String) match.get("home3");
                    String home4 = (String) match.get("home4");
                    String awayTeamScore = (String) match.get("awayScore");
                    String away1 = (String) match.get("away1");
                    String away2 = (String) match.get("away2");
                    String away3 = (String) match.get("away3");
                    String away4 = (String) match.get("away4");
                    Boolean hasStats = (Boolean) match.get("hasStats");
                    feiJingBasketballMatch.setHomeScore((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(homeTeamScore)));
                    feiJingBasketballMatch.setHFQuarter((StringUtils.equals("", home1) ? 0 : Integer.parseInt(home1)));
                    feiJingBasketballMatch.setHSQuarter((StringUtils.equals("", home2) ? 0 : Integer.parseInt(home2)));
                    feiJingBasketballMatch.setHTQuarter((StringUtils.equals("", home3) ? 0 : Integer.parseInt(home3)));
                    feiJingBasketballMatch.setH4Quarter((StringUtils.equals("", home4) ? 0 : Integer.parseInt(home4)));
                    feiJingBasketballMatch.setAwayScore((StringUtils.equals("", awayTeamScore) ? 0 : Integer.parseInt(awayTeamScore)));
                    feiJingBasketballMatch.setAFQuarter((StringUtils.equals("", away1) ? 0 : Integer.parseInt(away1)));
                    feiJingBasketballMatch.setASQuarter((StringUtils.equals("", away2) ? 0 : Integer.parseInt(away2)));
                    feiJingBasketballMatch.setATQuarter((StringUtils.equals("", away3) ? 0 : Integer.parseInt(away3)));
                    feiJingBasketballMatch.setA4Quarter((StringUtils.equals("", away4) ? 0 : Integer.parseInt(away4)));
                    feiJingBasketballMatch.setStatus(state.toString());
                    feiJingBasketballMatch.setHasState(hasStats);
                    feiJingBasketballMatchDao.updateMatchScoreByMatchId(feiJingBasketballMatch);
                }
            });
        }



    }
}
