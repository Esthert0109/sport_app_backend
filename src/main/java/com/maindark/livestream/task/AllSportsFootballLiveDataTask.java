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
        String result = HttpUtil.getNaMiData(url);
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
        Integer homeRedCardNum = 0;
        Integer awayRedCardNum = 0;
        Integer homeYellowCardNum = 0;
        Integer awayYellowCardNum = 0;
        Integer homeScore = 0;
        Integer awayScore = 0;
        if(statistics != null && !statistics.isEmpty()){
            for(int i = 0;i<statistics.size();i++){
                Map<String,Object> statistic = (Map<String,Object>)statistics.get(i);
                String type = (String)statistic.get("type");
                switch (type){
                    case "Attacks":
                        homeAttackNum = (String)statistic.get("home");
                        awayAttackNum = (String)statistic.get("away");
                        break;
                    case "Dangerous Attacks":
                        homeAttackDangerNum = (String)statistic.get("home");
                        awayAttackDangerNum = (String)statistic.get("away");
                        break;
                    case "Off Target":
                        homeBiasNum = (String)statistic.get("home");
                        awayBiasNum = (String)statistic.get("away");
                        break;
                    case "Shots Total":
                        homeShootGoalNum = (String)statistic.get("home");
                        awayShootGoalNum = (String)statistic.get("away");
                        break;
                    case "Ball Possession":
                        homePossessionRate = (String)statistic.get("home");
                        awayPossessionRate = (String)statistic.get("away");
                        break;
                    case "Corners":
                        homeCornerKickNum = (String)statistic.get("home");
                        awayCornerKickNum = (String)statistic.get("away");
                        break;
                }
            }
        }

        //  get home and away team card number
        JSONArray cards = (JSONArray) ml.get("cards");
        if(cards != null){
            int size = cards.size();
            for(int i=0;i<size;i++) {
                Map<String,Object> card = (Map<String,Object>)cards.get(i);
                String homeFault = (String)card.get("home_fault");
                String cardColor = (String)card.get("card");
                String awayFault = (String)card.get("away_fault");
                if(!StringUtils.equals("",homeFault)){
                    if(StringUtils.equals("yellow card",cardColor)) {
                        homeYellowCardNum +=1;
                    } else if(StringUtils.equals("red card",cardColor)){
                        homeRedCardNum += 1;
                    }
                }
                if(!StringUtils.equals("",awayFault)){
                    if(StringUtils.equals("yellow card",cardColor)) {
                        awayYellowCardNum +=1;
                    } else if(StringUtils.equals("red card",cardColor)){
                        awayRedCardNum += 1;
                    }
                }
            }
        }
        //Map<String,Object> lineups = (Map<String, Object>) ml.get("lineups");
        /*if (lineups != null && !lineups.isEmpty()) {
            // get home team red and yellow cards number
            Map<String, Object> homeTeam = (Map<String, Object>) lineups.get("home_team");
            if (homeTeam != null && !homeTeam.isEmpty()) {
                JSONArray startingLineups = (JSONArray) homeTeam.get("starting_lineups");
                JSONArray substitutes = (JSONArray) homeTeam.get("substitutes");
                Integer[] cards = getReadOrYellowCard(startingLineups,substitutes);
                homeYellowCardNum = cards[0];
                homeRedCardNum = cards[1];

            }
            // get away team red and yellow cards number
            Map<String, Object> awayTeam = (Map<String, Object>) lineups.get("away_team");
            if (awayTeam != null && !awayTeam.isEmpty()) {
                JSONArray startingLineups = (JSONArray) awayTeam.get("starting_lineups");
                JSONArray substitutes = (JSONArray) awayTeam.get("substitutes");
                Integer[] cards = getReadOrYellowCard(startingLineups,substitutes);
                awayYellowCardNum = cards[0];
                awayRedCardNum = cards[1];
            }
        }*/
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
        footballMatchLiveData.setRefereeName(refereeName);
        footballMatchLiveData.setVenueName(venueName);
        footballMatchLiveData.setStatus(status);
        footballMatchLiveData.setHomeAttackNum(Integer.parseInt(homeAttackNum));
        footballMatchLiveData.setAwayAttackNum(Integer.parseInt(awayAttackNum));
        footballMatchLiveData.setHomeAttackDangerNum(Integer.parseInt(homeAttackDangerNum));
        footballMatchLiveData.setAwayAttackDangerNum(Integer.parseInt(awayAttackDangerNum));
        footballMatchLiveData.setHomeBiasNum(Integer.parseInt(homeBiasNum));
        footballMatchLiveData.setAwayBiasNum(Integer.parseInt(awayBiasNum));
        footballMatchLiveData.setHomeCornerKickNum(Integer.parseInt(homeCornerKickNum));
        footballMatchLiveData.setAwayCornerKickNum(Integer.parseInt(awayCornerKickNum));
        footballMatchLiveData.setHomePossessionRate(Integer.parseInt(homePossessionRate));
        footballMatchLiveData.setAwayPossessionRate(Integer.parseInt(awayPossessionRate));
        footballMatchLiveData.setHomeShootGoalNum(Integer.parseInt(homeShootGoalNum));
        footballMatchLiveData.setAwayShootGoalNum(Integer.parseInt(awayShootGoalNum));
        footballMatchLiveData.setHomeYellowCardNum(homeYellowCardNum);
        footballMatchLiveData.setAwayYellowCardNum(awayYellowCardNum);
        footballMatchLiveData.setHomeRedCardNum(homeRedCardNum);
        footballMatchLiveData.setAwayRedCardNum(awayRedCardNum);
        footballMatchLiveData.setHomeScore(homeScore);
        footballMatchLiveData.setAwayScore(awayScore);
        return footballMatchLiveData;
    }

    private Integer[] getReadOrYellowCard(JSONArray startingLineups, JSONArray substitutes) {
        Integer yellowCardNum = 0;
        Integer redCardNum = 0;
        Integer[] cards = {0,0};
        if(startingLineups != null){
            for(int i=0;i<startingLineups.size();i++){
                Map<String,Object> map = (Map<String,Object>)startingLineups.get(i);
                Number playerKey = (Number)map.get("player_key");
                Integer[] cardsArr = getPlayerCardById(playerKey.toString());
                yellowCardNum += cardsArr[0];
                redCardNum += cardsArr[2];
            }
        }
        if(substitutes != null){
            for(int i=0;i<substitutes.size();i++){
                Map<String,Object> map = (Map<String,Object>)startingLineups.get(i);
                Number playerKey = (Number)map.get("player_key");
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
        String result = HttpUtil.getNaMiData(url);
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
