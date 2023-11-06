package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsFootballLiveDataDao;
import com.maindark.livestream.domain.AllSportsFootballMatchLiveData;
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
public class AllSportsFootballLiveDataTask {
    @Resource
    AllSportsConfig allSportsConfig;
    @Resource
    AllSportsFootballLiveDataDao allSportsFootballLiveDataDao;
    @Scheduled(cron = "0 */2 * * * ? ")
    public void getAllSportsFootballLiveData() {
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLivescore());
        String result = HttpUtil.getAllSportsData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if (success != null) {
                if (1 == success) {
                    List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                    if (matches != null && !matches.isEmpty()) {
                        matches.stream().forEach(ml ->{
                            String eventLive = (String)ml.get("event_live");
                            if(StringUtils.equals("1",eventLive)) {
                                Number matchId = (Number) ml.get("event_key");
                                int exist = allSportsFootballLiveDataDao.queryExist(matchId.longValue());
                                AllSportsFootballMatchLiveData footballMatchLiveData = getAllSportsLiveData(ml);
                                if(exist <=0) {
                                    allSportsFootballLiveDataDao.insert(footballMatchLiveData);
                                } else {
                                    allSportsFootballLiveDataDao.updateData(footballMatchLiveData);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    private AllSportsFootballMatchLiveData getAllSportsLiveData(Map<String, Object> ml) {
        AllSportsFootballMatchLiveData footballMatchLiveData = new AllSportsFootballMatchLiveData();
        Number matchId = (Number) ml.get("event_key");
        String status = (String)ml.get("event_status");
        String matchDate = (String)ml.get("event_date");
        String matchTime = (String)ml.get("event_time");
        String homeTeamName = (String)ml.get("event_home_team");
        String awayTeamName = (String)ml.get("event_away_team");
        String homeTeamLogo = (String)ml.get("home_team_logo");
        String awayTeamLogo = (String)ml.get("away_team_logo");
        String homeFormation = (String)ml.get("event_home_formation");
        String awayFormation = (String)ml.get("event_away_formation");
        String refereeName = (String)ml.get("event_referee");
        String venueName = (String)ml.get("event_stadium");
        JSONArray statistics= (JSONArray) ml.get("statistics");
        String homeAttackNum = "0";
        String awayAttackNum = "0";
        String homeAttackDangerNum = "0";
        String awayAttackDangerNum = "0";
        String homePossessionRate = "0";
        String awayPossessionRate = "0";
        String homeShootGoalNum = "0";
        String awayShootGoalNum = "0";
        String homeBiasNum = "0";
        String awayBiasNum = "0";
        String homeCornerKickNum = "0";
        String awayCornerKickNum = "0";
        String homeRedCardNum = "0";
        String awayRedCardNum = "0";
        String homeYellowCardNum = "0";
        String awayYellowCardNum = "0";
        String homePenaltyNum = "0";
        String awayPenaltyNum = "0";
        Integer homeScore = 0;
        Integer awayScore = 0;
        String homeCoach = "";
        String awayCoach = "";
        if(statistics != null && !statistics.isEmpty()){
            for (Object o : statistics) {
                Map<String, Object> statistic = (Map<String, Object>) o;
                String type = (String) statistic.get("type");
                switch (type) {
                    case "Attacks":
                        homeAttackNum = (String) statistic.get("home");
                        awayAttackNum = (String) statistic.get("away");
                        break;
                    case "Dangerous Attacks":
                        homeAttackDangerNum = (String) statistic.get("home");
                        awayAttackDangerNum = (String) statistic.get("away");
                        break;
                    case "Off Target":
                        homeBiasNum = (String) statistic.get("home");
                        awayBiasNum = (String) statistic.get("away");
                        break;
                    case "Shots On Goal":
                        homeShootGoalNum = (String) statistic.get("home");
                        awayShootGoalNum = (String) statistic.get("away");
                        break;
                    case "Ball Possession":
                        homePossessionRate = (String) statistic.get("home");
                        awayPossessionRate = (String) statistic.get("away");
                        break;
                    case "Corners":
                        homeCornerKickNum = (String) statistic.get("home");
                        awayCornerKickNum = (String) statistic.get("away");
                        break;
                    case "Yellow Cards":
                        homeYellowCardNum = (String) statistic.get("home");
                        awayYellowCardNum = (String) statistic.get("away");
                        break;
                    case "Red Cards":
                        homeRedCardNum = (String) statistic.get("home");
                        awayRedCardNum = (String) statistic.get("away");
                        break;
                    case "Penalty":
                        homePenaltyNum = (String) statistic.get("home");
                        awayPenaltyNum = (String) statistic.get("away");
                        break;

                }
            }
        }


        Map<String, Object> lineups = (Map<String, Object>) ml.get("lineups");
        if (lineups != null && !lineups.isEmpty()) {
            // set home coach
            Map<String, Object> homeTeam = (Map<String, Object>) lineups.get("home_team");
            if (homeTeam != null && !homeTeam.isEmpty()) {
                JSONArray coaches = (JSONArray) homeTeam.get("coaches");
                if(coaches != null && !coaches.isEmpty()){
                    Map<String,Object> coachMap = (Map<String,Object>)coaches.get(0);
                    homeCoach = (String)coachMap.get("coache");
                }
            }
            // set away coach
            Map<String, Object> awayTeam = (Map<String, Object>) lineups.get("away_team");
            if (awayTeam != null && !awayTeam.isEmpty()) {
                JSONArray coaches = (JSONArray) awayTeam.get("coaches");
                if(coaches != null && !coaches.isEmpty()){
                    Map<String,Object> coachMap = (Map<String,Object>)coaches.get(0);
                    awayCoach = (String)coachMap.get("coache");
                }
            }
        }
        String scores = (String)ml.get("event_final_result");
        if(!StringUtils.equals("",scores)){
            String[] scoreArr = scores.split("-");
            if(scoreArr != null && scoreArr.length >0) {
                homeScore = Integer.parseInt(scoreArr[0].trim());
                awayScore = Integer.parseInt(scoreArr[1].trim());
            }
        }


        footballMatchLiveData.setMatchId(matchId.longValue());
        footballMatchLiveData.setMatchTime(matchTime);
        footballMatchLiveData.setMatchDate(matchDate);
        footballMatchLiveData.setHomeTeamName(homeTeamName);
        footballMatchLiveData.setAwayTeamName(awayTeamName);
        footballMatchLiveData.setHomeTeamLogo(homeTeamLogo);
        footballMatchLiveData.setAwayTeamLogo(awayTeamLogo);
        footballMatchLiveData.setHomeFormation(homeFormation);
        footballMatchLiveData.setAwayFormation(awayFormation);
        footballMatchLiveData.setHomeCoach(homeCoach);
        footballMatchLiveData.setAwayCoach(awayCoach);
        footballMatchLiveData.setRefereeName(refereeName);
        footballMatchLiveData.setVenueName(venueName);
        footballMatchLiveData.setStatus(status);
        footballMatchLiveData.setHomeAttackNum(StringUtils.equals("",homeAttackNum)?0:Integer.parseInt(homeAttackNum));
        footballMatchLiveData.setAwayAttackNum(StringUtils.equals("",awayAttackNum)?0:Integer.parseInt(awayAttackNum));
        footballMatchLiveData.setHomeAttackDangerNum(StringUtils.equals("",homeAttackDangerNum)?0:Integer.parseInt(homeAttackDangerNum));
        footballMatchLiveData.setAwayAttackDangerNum(StringUtils.equals("",awayAttackDangerNum)?0:Integer.parseInt(awayAttackDangerNum));
        footballMatchLiveData.setHomeBiasNum(StringUtils.equals("",homeBiasNum)?0:Integer.parseInt(homeBiasNum));
        footballMatchLiveData.setAwayBiasNum(StringUtils.equals("",awayBiasNum)?0:Integer.parseInt(awayBiasNum));
        footballMatchLiveData.setHomeCornerKickNum(StringUtils.equals("",homeCornerKickNum)?0:Integer.parseInt(homeCornerKickNum));
        footballMatchLiveData.setAwayCornerKickNum(StringUtils.equals("",awayCornerKickNum)?0:Integer.parseInt(awayCornerKickNum));
        footballMatchLiveData.setHomePossessionRate(homePossessionRate);
        footballMatchLiveData.setAwayPossessionRate(awayPossessionRate);
        footballMatchLiveData.setHomeShootGoalNum(StringUtils.equals("",awayShootGoalNum)?0:Integer.parseInt(homeShootGoalNum));
        footballMatchLiveData.setAwayShootGoalNum(StringUtils.equals("",awayShootGoalNum)?0:Integer.parseInt(awayShootGoalNum));
        footballMatchLiveData.setHomeYellowCardNum(StringUtils.equals("",homeYellowCardNum)?0:Integer.parseInt(homeYellowCardNum));
        footballMatchLiveData.setAwayYellowCardNum(StringUtils.equals("",awayYellowCardNum)?0:Integer.parseInt(awayYellowCardNum));
        footballMatchLiveData.setHomeRedCardNum(StringUtils.equals("",homeRedCardNum)?0:Integer.parseInt(homeRedCardNum));
        footballMatchLiveData.setAwayRedCardNum(StringUtils.equals("",awayRedCardNum)?0:Integer.parseInt(awayRedCardNum));
        footballMatchLiveData.setHomeScore(homeScore);
        footballMatchLiveData.setAwayScore(awayScore);
        footballMatchLiveData.setHomePenaltyNum(StringUtils.equals("",homePenaltyNum)?0:Integer.parseInt(homePenaltyNum));
        footballMatchLiveData.setAwayPenaltyNum(StringUtils.equals("",awayPenaltyNum)?0:Integer.parseInt(awayPenaltyNum));
        return footballMatchLiveData;
    }

    private Integer[] getReadOrYellowCard(JSONArray startingLineups, JSONArray substitutes) {
        Integer yellowCardNum = 0;
        Integer redCardNum = 0;
        Integer[] cards = {0,0};
        if(startingLineups != null){
            for (Object startingLineup : startingLineups) {
                Map<String, Object> map = (Map<String, Object>) startingLineup;
                Number playerKey = (Number) map.get("player_key");
                Integer[] cardsArr = getPlayerCardById(playerKey.toString());
                yellowCardNum += cardsArr[0];
                redCardNum += cardsArr[2];
            }
        }
        if(substitutes != null){
            for (Object substitute : substitutes) {
                Map<String, Object> map = (Map<String, Object>) substitute;
                Number playerKey = (Number) map.get("player_key");
                Integer[] cardsArr = getPlayerCardById(playerKey.toString());
                yellowCardNum += cardsArr[0];
                redCardNum += cardsArr[2];
            }
        }
        cards[0] = yellowCardNum;
        cards[1] = redCardNum;
        return cards;
    }

    private Integer[] getPlayerCardById(String playId) {
        Integer[] arr = {0,0};
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getPlayers()).replace("{}",playId);
        String result = HttpUtil.getAllSportsData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if (success != null) {
                if (1 == success) {
                    JSONArray playArray = (JSONArray) resultObj.get("result");
                    if (playArray != null && !playArray.isEmpty()) {
                        Map<String,Object> playMap = (Map<String, Object>) playArray.get(0);
                        String yellowCards = (String)playMap.get("player_yellow_cards");
                        if(StringUtils.equals("",yellowCards)){
                            yellowCards = "0";
                        }
                        String redCards = (String)playMap.get("player_red_cards");
                        if(StringUtils.equals("",redCards)){
                            redCards = "0";
                        }
                        arr[0] = Integer.parseInt(yellowCards);
                        arr[1] = Integer.parseInt(redCards);
                    }
                }
            }
        }
        return arr;
    }
}
