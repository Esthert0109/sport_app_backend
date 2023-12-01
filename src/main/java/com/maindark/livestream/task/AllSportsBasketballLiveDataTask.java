package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsBasketballMatchLiveDataDao;
import com.maindark.livestream.domain.AllSportsBasketballMatchLiveData;
import com.maindark.livestream.txYun.TxYunConfig;
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
    AllSportsBasketballMatchLiveDataDao allSportsBasketballMatchLiveDataDao;

    @Resource
    TxYunConfig txYunConfig;

    //@Scheduled(cron = "0 */2 * * * ?")
    public void getAllLiveData() {
        String url = allSportsConfig.getAllSportsBasketballApi(allSportsConfig.getBasketballLiveDataUrl());
        String result = HttpUtil.getAllSportsData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if (success != null && 1 == success) {
                List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                if (matches != null && !matches.isEmpty()) {
                    matches.stream().forEach(ml -> {
                        AllSportsBasketballMatchLiveData allSportsBasketballMatchLiveData = insertOrUpdateLiveData(ml);
                        int exist = allSportsBasketballMatchLiveDataDao.queryExist(allSportsBasketballMatchLiveData.getMatchId());
                        if(exist <=0) {
                            allSportsBasketballMatchLiveDataDao.insertData(allSportsBasketballMatchLiveData);
                        } else {
                            allSportsBasketballMatchLiveDataDao.updateData(allSportsBasketballMatchLiveData);
                        }
                    });
                }
            }
        }
    }

    private AllSportsBasketballMatchLiveData insertOrUpdateLiveData(Map<String, Object> ml) {
        AllSportsBasketballMatchLiveData allSportsBasketballMatchLiveData = new AllSportsBasketballMatchLiveData();
        Number matchId = (Number) ml.get("event_key");
        String status = (String)ml.get("event_status");
        String matchTime = (String) ml.get("event_time");
        String matchDate = (String) ml.get("event_date");
        String homeTeamName = (String) ml.get("event_home_team");
        String awayTeamName = (String) ml.get("event_away_team");
        String homeTeamLogo = (String) ml.get("event_home_team_logo");
        String awayTeamLogo = (String) ml.get("event_away_team_logo");

        Map<String, Object> scores = (Map<String, Object>) ml.get("scores");
        Integer hFQuarter = 0;
        Integer hSQuarter = 0;
        Integer hTQuarter = 0;
        Integer h4Quarter = 0;
        Integer aFQuarter = 0;
        Integer aSQuarter = 0;
        Integer aTQuarter = 0;
        Integer a4Quarter = 0;
        String hBlocks = "";
        String hFieldGoals = "";
        String hFreeThrows = "";
        String hPersonalFouls = "";
        String hRebounds = "";
        String hSteals = "";
        String hThreePointGoals = "";
        String hTurnOvers = "";
        String aBlocks = "";
        String aFieldGoals = "";
        String aFreeThrows = "";
        String aPersonalFouls = "";
        String aRebounds = "";
        String aSteals = "";
        String aThreePointGoals = "";
        String aTurnOvers = "";
        if (scores != null && !scores.isEmpty()) {
            JSONArray fQuarter = (JSONArray) scores.get("1stQuarter");
            JSONArray sQuarter = (JSONArray) scores.get("2ndQuarter");
            JSONArray tQuarter = (JSONArray) scores.get("3rdQuarter");
            JSONArray f4Quarter = (JSONArray) scores.get("4thQuarter");
            if (fQuarter != null && !fQuarter.isEmpty()) {
                for (Object o : fQuarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    hFQuarter = Integer.valueOf((String) map.get("score_home"));
                    aFQuarter = Integer.valueOf((String) map.get("score_away"));
                }
            }
            if (sQuarter != null && !sQuarter.isEmpty()) {
                for (Object o : sQuarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    hSQuarter = Integer.valueOf((String) map.get("score_home"));
                    aSQuarter = Integer.valueOf((String) map.get("score_away"));
                }
            }
            if (tQuarter != null && !tQuarter.isEmpty()) {
                for (Object o : tQuarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    hTQuarter = Integer.valueOf((String) map.get("score_home"));
                    aTQuarter = Integer.valueOf((String) map.get("score_away"));
                }
            }
            if (f4Quarter != null && !f4Quarter.isEmpty()) {
                for (Object o : f4Quarter) {
                    Map<String, Object> map = (Map<String, Object>) o;
                    h4Quarter = Integer.valueOf((String) map.get("score_home"));
                    a4Quarter = Integer.valueOf((String) map.get("score_away"));
                }
            }
        }
        String score = (String) ml.get("event_final_result");
        if (!StringUtils.equals("", score)) {
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
        JSONArray statistics = (JSONArray) ml.get("statistics");
        if (statistics != null && !statistics.isEmpty()) {
            for (Object o : statistics) {
                Map<String, Object> statistic = (Map<String, Object>) o;
                String type = (String) statistic.get("type");
                switch (type) {
                    case "Total Blocks":
                        hBlocks = (String) statistic.get("home");
                        aBlocks = (String) statistic.get("away");
                        break;
                    case "Total Field Goals":
                        hFieldGoals = (String) statistic.get("home");
                        aFieldGoals = (String) statistic.get("away");
                        break;
                    case "Total FreeThrows":
                        hFreeThrows = (String) statistic.get("home");
                        aFreeThrows = (String) statistic.get("away");
                        break;
                    case "Total Personal Fouls":
                        hPersonalFouls = (String) statistic.get("home");
                        aPersonalFouls = (String) statistic.get("away");
                        break;
                    case "Total Rebounds":
                        hRebounds = (String) statistic.get("home");
                        aRebounds = (String) statistic.get("away");
                        break;
                    case "Total Steals":
                        hSteals = (String) statistic.get("home");
                        aSteals = (String) statistic.get("away");
                        break;
                    case "Total Turnovers":
                        hTurnOvers = (String) statistic.get("home");
                        aTurnOvers = (String) statistic.get("away");
                        break;
                    case "Total Threepoint Goals":
                        hThreePointGoals = (String) statistic.get("home");
                        aThreePointGoals = (String) statistic.get("away");
                        break;
                }


            }
        }
        allSportsBasketballMatchLiveData.setMatchId(matchId.longValue());
        allSportsBasketballMatchLiveData.setHFQuarter(hFQuarter);
        allSportsBasketballMatchLiveData.setHSQuarter(hSQuarter);
        allSportsBasketballMatchLiveData.setHTQuarter(hTQuarter);
        allSportsBasketballMatchLiveData.setH4Quarter(h4Quarter);
        allSportsBasketballMatchLiveData.setAFQuarter(aFQuarter);
        allSportsBasketballMatchLiveData.setASQuarter(aSQuarter);
        allSportsBasketballMatchLiveData.setATQuarter(aTQuarter);
        allSportsBasketballMatchLiveData.setA4Quarter(a4Quarter);
        allSportsBasketballMatchLiveData.setHBlocks(hBlocks);
        allSportsBasketballMatchLiveData.setHFieldGoals(hFieldGoals);
        allSportsBasketballMatchLiveData.setHSteals(hSteals);
        allSportsBasketballMatchLiveData.setHFreeThrows(hFreeThrows);
        allSportsBasketballMatchLiveData.setHThreePointGoals(hThreePointGoals);
        allSportsBasketballMatchLiveData.setHRebounds(hRebounds);
        allSportsBasketballMatchLiveData.setHPersonalFouls(hPersonalFouls);
        allSportsBasketballMatchLiveData.setHTurnOvers(hTurnOvers);
        allSportsBasketballMatchLiveData.setABlocks(aBlocks);
        allSportsBasketballMatchLiveData.setAFieldGoals(aFieldGoals);
        allSportsBasketballMatchLiveData.setASteals(aSteals);
        allSportsBasketballMatchLiveData.setARebounds(aRebounds);
        allSportsBasketballMatchLiveData.setAFreeThrows(aFreeThrows);
        allSportsBasketballMatchLiveData.setAPersonalFouls(aPersonalFouls);
        allSportsBasketballMatchLiveData.setATurnOvers(aTurnOvers);
        allSportsBasketballMatchLiveData.setAThreePointGoals(aThreePointGoals);
        allSportsBasketballMatchLiveData.setStatus(status);
        allSportsBasketballMatchLiveData.setMatchTime(matchTime);
        allSportsBasketballMatchLiveData.setMatchDate(matchDate);
        allSportsBasketballMatchLiveData.setHomeTeamName(homeTeamName);
        allSportsBasketballMatchLiveData.setAwayTeamName(awayTeamName);
        allSportsBasketballMatchLiveData.setHomeTeamLogo(ml.get("event_home_team_logo") == null?txYunConfig.getDefaultLogo():(String)ml.get("event_home_team_logo"));
        allSportsBasketballMatchLiveData.setAwayTeamLogo(ml.get("event_away_team_logo") == null?txYunConfig.getDefaultLogo():(String)ml.get("event_away_team_logo"));
        return allSportsBasketballMatchLiveData;
    }

}
