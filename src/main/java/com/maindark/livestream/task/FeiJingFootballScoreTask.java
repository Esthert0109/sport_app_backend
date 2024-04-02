package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
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
public class FeiJingFootballScoreTask {

    @Resource
    FeiJingConfig feiJingConfig;
    @Resource
    FeiJingFootballMatchDao feiJingFootballMatchDao;

    @Scheduled(cron = "*/20 * * * * *")
    public void changeFootballMatchScore(){
        String url = feiJingConfig.getTodayMatch();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> changeList = (List<Map<String,Object>>) resultObj.get("changeList");
            if(changeList != null && !changeList.isEmpty()) {
                changeList.forEach(match->{
                    Integer matchId = (Integer) match.get("matchId");
                    int existed = feiJingFootballMatchDao.queryExisted(matchId);
                    if(existed >0) {
                        FeiJingFootballMatch feiJingFootballMatch = new FeiJingFootballMatch();
                        Integer state = (Integer)match.get("state");
                        Integer homeScore = (Integer)match.get("homeScore");
                        Integer awayScore = (Integer)match.get("awayScore");
                        String hasLineup = (String) match.get("hasLineup");
                        feiJingFootballMatch.setStatusId(state);
                        feiJingFootballMatch.setHomeTeamScore(homeScore);
                        feiJingFootballMatch.setAwayTeamScore(awayScore);
                        feiJingFootballMatch.setLineUp(StringUtils.equals("",hasLineup)?0:1);
                        feiJingFootballMatchDao.updateMatchScoreByMatchId(feiJingFootballMatch);
                    }
                });
            }
        }

    }

}
