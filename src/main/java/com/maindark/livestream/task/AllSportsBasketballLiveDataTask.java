package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsBasketballMatchDao;
import com.maindark.livestream.domain.AllSportsBasketballMatchLiveData;
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
public class AllSportsBasketballLiveDataTask {
    @Resource
    AllSportsConfig allSportsConfig;
    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

    @Scheduled(cron = "0 */2 * * * ?")
    public void getAllLiveData() {
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getBasketballLiveDataUrl());
        String result = HttpUtil.getAllSportsData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if (success != null && 1 == success) {
                List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                if (matches != null && !matches.isEmpty()) {
                    matches.stream().forEach(ml -> {
                        AllSportsBasketballMatchLiveData allSportsBasketballMatchLiveData = insertOrUpdateLiveData(ml);
                    });
                }
            }
        }
    }

    private AllSportsBasketballMatchLiveData insertOrUpdateLiveData(Map<String, Object> ml) {
        AllSportsBasketballMatchLiveData allSportsBasketballMatchLiveData = new AllSportsBasketballMatchLiveData();
        Number matchId = (Number) ml.get("event_key");
        Map<String, Object> scores = (Map<String, Object>) ml.get("scores");
        Integer hFQuarter = 0;
        Integer hSQuarter = 0;
        Integer hTQuarter = 0;
        Integer h4Quarter = 0;
        Integer aFQuarter = 0;
        Integer aSQuarter = 0;
        Integer aTQuarter = 0;
        Integer a4Quarter = 0;
        Integer hPKickGoal = 0;
        Integer hNumPauseRemain = 0;
        Integer hNumOfFouls = 0;
        Integer hFreeThrowPercentage = 0;
        Integer hTotalPause = 0;
        Integer hThreeGoal = 0;
        Integer hTwoGoal = 0;
        Integer aPKickGoal = 0;
        Integer aNumPauseRemain = 0;
        Integer aNumOfFouls = 0;
        Integer aFreeThrowPercentage = 0;
        Integer aTotalPause = 0;
        Integer aThreeGoal = 0;
        Integer aTwoGoal = 0;

        if(scores != null && !scores.isEmpty()) {
            JSONArray fQuarter = (JSONArray) scores.get("1stQuarter");
            JSONArray sQuarter = (JSONArray) scores.get("1stQuarter");
            JSONArray tQuarter = (JSONArray) scores.get("1stQuarter");
            JSONArray f4Quarter = (JSONArray) scores.get("1stQuarter");
            if(fQuarter != null && !fQuarter.isEmpty()) {
                for (Object o : fQuarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    hFQuarter = Integer.valueOf((String)map.get("score_home"));
                    aFQuarter = Integer.valueOf((String)map.get("score_away"));
                }
            }
            if(sQuarter != null && !sQuarter.isEmpty()) {
                for (Object o : sQuarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    hSQuarter = Integer.valueOf((String)map.get("score_home"));
                    aSQuarter = Integer.valueOf((String)map.get("score_away"));
                }
            }
            if(tQuarter != null && !tQuarter.isEmpty()) {
                for (Object o : tQuarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    hTQuarter = Integer.valueOf((String)map.get("score_home"));
                    aTQuarter = Integer.valueOf((String)map.get("score_away"));
                }
            }
            if(f4Quarter != null && !f4Quarter.isEmpty()) {
                for (Object o : f4Quarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    h4Quarter = Integer.valueOf((String)map.get("score_home"));
                    a4Quarter = Integer.valueOf((String)map.get("score_away"));
                }
            }
        }
        String score = (String) ml.get("event_final_result");
        if(!StringUtils.equals("",score)) {
            String[] scoreArr = score.split("-");
            if (scoreArr.length > 0) {
                allSportsBasketballMatchLiveData.setHomeScore(Integer.parseInt(scoreArr[0].trim()));
                allSportsBasketballMatchLiveData.setAwayScore(Integer.parseInt(scoreArr[1].trim()));
            }
        } else {
            Integer homeScore = hFQuarter + hSQuarter + hTQuarter + h4Quarter;
            Integer awayScore = aFQuarter + aSQuarter + aTQuarter + a4Quarter;
            allSportsBasketballMatchLiveData.setHomeScore(homeScore);
            allSportsBasketballMatchLiveData.setAwayScore(awayScore);
        }
        JSONArray statistics= (JSONArray) ml.get("statistics");
        if(statistics != null && !statistics.isEmpty()) {
            for (Object o : statistics) {
                Map<String, Object> statistic = (Map<String, Object>) o;
                String type = (String) statistic.get("type");
//                Field Goal Attempts
//                Attempts FreeThrows
//                Attempts Threepoint Goals
//                Defense Rebounds
//                Offense Rebounds
//                Field Goals Pct
//                FreeThrows Pct
//                Pct Threepoint Goals
//                Total Assists
//                Total Blocks
//                Total Field Goals
//                Total FreeThrows
//                Total Personal Fouls
//                Total Rebounds
//                Total Steals
//                Total Threepoint Goals
//                Total Turnovers

                /* 罚球进球数 */
//                private Integer hPKickGoal;
//                /*剩余暂停数*/
//                private Integer hNumPauseRemain;
//                /*  犯规数 */
//                private Integer hNumOfFouls;
//                /* 罚球命中率 */
//                private Integer hFreeThrowPercentage;
//                /* 总暂停数 */
//                private Integer hTotalPause;
//                /* 3分球进球数*/
//                private Integer hThreeGoal;
                switch (type) {
                    case "Attacks":
                        hThreeGoal = Integer.parseInt((String) statistic.get("home"));
                        aThreeGoal = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "Dangerous Attacks":
                        hThreeGoal = Integer.parseInt((String) statistic.get("home"));
                        aThreeGoal = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "Off Target":
                        hThreeGoal = Integer.parseInt((String) statistic.get("home"));
                        aThreeGoal = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "Shots On Goal":
                        hThreeGoal = Integer.parseInt((String) statistic.get("home"));
                        aThreeGoal = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "Ball Possession":
                        hThreeGoal = Integer.parseInt((String) statistic.get("home"));
                        aThreeGoal = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "FreeThrows Pct":
                        hFreeThrowPercentage = Integer.parseInt((String) statistic.get("home"));
                        aFreeThrowPercentage = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "FreeThrows":
                        hPKickGoal = Integer.parseInt((String) statistic.get("home"));
                        aPKickGoal = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "Total Personal Fouls":
                        hNumOfFouls = Integer.parseInt((String) statistic.get("home"));
                        aNumOfFouls = Integer.parseInt((String) statistic.get("away"));
                        break;
                    case "Total Threepoint Goals":
                        hThreeGoal = Integer.parseInt((String) statistic.get("home"));
                        aThreeGoal = Integer.parseInt((String) statistic.get("away"));
                        break;

                }
            }
        }
        return null;
    }


}
