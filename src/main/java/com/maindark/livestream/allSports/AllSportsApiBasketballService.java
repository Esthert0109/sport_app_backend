package com.maindark.livestream.allSports;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.dao.AllSportsBasketballLineUpDao;
import com.maindark.livestream.dao.AllSportsBasketballMatchDao;
import com.maindark.livestream.dao.AllSportsBasketballMatchLiveDataDao;
import com.maindark.livestream.domain.AllSportsBasketballLineUp;
import com.maindark.livestream.domain.AllSportsBasketballMatch;
import com.maindark.livestream.domain.AllSportsBasketballMatchLiveData;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.txYun.TxYunConfig;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.util.StreamToListUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class AllSportsApiBasketballService {
    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

    @Resource
    AllSportsBasketballMatchLiveDataDao allSportsBasketballMatchLiveDataDao;

    @Resource
    AllSportsBasketballLineUpDao allSportsBasketballLineUpDao;

    @Resource
    TxYunConfig txYunConfig;

    public List<AllSportsBasketballMatch> getAllFixtures(String from, String to) {
        String url = allSportsConfig.getAllSportsBasketballApi(allSportsConfig.getFixtures()) + "&from=" + from + "&to=" + to;
        List<AllSportsBasketballMatch> allSportsBasketballMatchList = getAllSportsMatch(url);
        if (allSportsBasketballMatchList != null) {
            allSportsBasketballMatchList.forEach(match -> {
                int exist = allSportsBasketballMatchDao.queryExist(match.getMatchId());
                if (exist <= 0) {
                    allSportsBasketballMatchDao.insertData(match);
                } else {
                    allSportsBasketballMatchDao.updateData(match);
                }
            });
        }
        return allSportsBasketballMatchList;
    }

    private List<AllSportsBasketballMatch> getAllSportsMatch(String url){
        List<AllSportsBasketballMatch> list = null;
        String result = HttpUtil.getAllSportsData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer)resultObj.get("success");
            if(1 == success) {
                List<Map<String,Object>> matchesList = (List<Map<String,Object>>)resultObj.get("result");
                if(matchesList != null && !matchesList.isEmpty()){
                    Stream<AllSportsBasketballMatch> basketballMatchVoStream = matchesList.stream().map(ml ->{
                        AllSportsBasketballMatch allSportsBasketballMatch = new AllSportsBasketballMatch();
                        Number eventKey = (Number)ml.get("event_key");
                        allSportsBasketballMatch.setMatchId(eventKey.longValue());
                        allSportsBasketballMatch.setHomeTeamId(((Number)ml.get("home_team_key")).longValue());
                        allSportsBasketballMatch.setAwayTeamId(((Number)ml.get("away_team_key")).longValue());
                        allSportsBasketballMatch.setMatchTime((String)ml.get("event_time"));
                        allSportsBasketballMatch.setHomeTeamLogo(ml.get("event_home_team_logo") == null?txYunConfig.getDefaultLogo():(String)ml.get("event_home_team_logo"));
                        allSportsBasketballMatch.setAwayTeamLogo((ml.get("event_away_team_logo") == null?txYunConfig.getDefaultLogo():(String)ml.get("event_away_team_logo"));
                        allSportsBasketballMatch.setHomeTeamName((String)ml.get("event_home_team"));
                        allSportsBasketballMatch.setAwayTeamName((String)ml.get("event_away_team"));
                        allSportsBasketballMatch.setMatchDate((String)ml.get("event_date"));
                        String matchStatus = (String)ml.get("event_status");
                        String eventLive = (String)ml.get("event_live");
                        String season = (String)ml.get("league_season");
                        if(StringUtils.equals(matchStatus,"")){
                            allSportsBasketballMatch.setHomeScore(0);
                            allSportsBasketballMatch.setAwayScore(0);
                            allSportsBasketballMatch.setStatus("");
                        } else {
                            String scores = (String)ml.get("event_final_result");
                            if(StringUtils.equals("",scores)){
                                allSportsBasketballMatch.setHomeScore(0);
                                allSportsBasketballMatch.setAwayScore(0);
                            } else {
                                String[] scoreArr = scores.split("-");
                                if(scoreArr.length >0) {
                                    allSportsBasketballMatch.setHomeScore(Integer.parseInt(scoreArr[0].trim()));
                                    allSportsBasketballMatch.setAwayScore(Integer.parseInt(scoreArr[1].trim()));
                                } else {
                                    allSportsBasketballMatch.setHomeScore(0);
                                    allSportsBasketballMatch.setAwayScore(0);
                                }
                            }
                            allSportsBasketballMatch.setStatus((String)ml.get("event_status"));
                        }


                        // if eventLive equals 1 , is playing now
                        allSportsBasketballMatch.setCompetitionName((String)ml.get("league_name"));
                        allSportsBasketballMatch.setCompetitionId(((Number)ml.get("league_key")).longValue());
                        allSportsBasketballMatch.setSeason(season);
                        allSportsBasketballMatch.setEventLive(eventLive);
                        return allSportsBasketballMatch;
                    });
                    list = StreamToListUtil.getArrayListFromStream(basketballMatchVoStream);
                }

            }
        }
        return list;
    }

    public List<AllSportsBasketballMatchLiveData> getAllFixturesPastLiveData(String from, String to) {
        String url = allSportsConfig.getAllSportsBasketballApi(allSportsConfig.getFixtures()) + "&from=" + from + "&to=" + to;
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
        return null;

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
        allSportsBasketballMatchLiveData.setHomeTeamLogo(homeTeamLogo);
        allSportsBasketballMatchLiveData.setAwayTeamLogo(awayTeamLogo);
        return allSportsBasketballMatchLiveData;
    }

    public Map<String, Object> getAllFixturesPastLineUp(String from, String to) {
        String url = allSportsConfig.getAllSportsBasketballApi(allSportsConfig.getFixtures()) + "&from=" + from + "&to=" + to;
        String result = HttpUtil.getAllSportsData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if (success != null) {
                if (1 == success) {
                    List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                    if (matches != null && !matches.isEmpty()) {
                        for (Map<String, Object> ml : matches) {


                                AllSportsBasketballMatch allSportsBasketballMatch = getAllSportsMatch(ml);
                                int exists = allSportsBasketballMatchDao.queryExist(allSportsBasketballMatch.getMatchId());
                                if (exists <= 0) {
                                    allSportsBasketballMatchDao.insertData(allSportsBasketballMatch);
                                } else {
                                    allSportsBasketballMatchDao.updateData(allSportsBasketballMatch);
                                }
                                Map<String, Object> lineups = (Map<String, Object>) ml.get("player_statistics");
                                Number matchId = (Number) ml.get("event_key");
                                if (lineups != null && !lineups.isEmpty()) {
                                    // set home team line-up
                                    JSONArray homeTeamLineUpArray = (JSONArray) lineups.get("home_team");
                                    getMatchLineUp(homeTeamLineUpArray, matchId.longValue(), LineUpType.HOME.getType());
                                    // set away team line-up
                                    JSONArray awayTeamLineUpArray = (JSONArray) lineups.get("away_team");
                                    getMatchLineUp(awayTeamLineUpArray, matchId.longValue(), LineUpType.AWAY.getType());
                                }
                            }
                        }
                    }
                } else {
                    log.info("there is no live data(basketball) now!");
                }
            }
        return resultObj;
    }



    private AllSportsBasketballMatch getAllSportsMatch(Map<String, Object> ml) {
        AllSportsBasketballMatch allSportsBasketballMatch = new AllSportsBasketballMatch();
        Number eventKey = (Number) ml.get("event_key");
        allSportsBasketballMatch.setMatchId(eventKey.longValue());
        allSportsBasketballMatch.setHomeTeamId(((Number) ml.get("home_team_key")).longValue());
        allSportsBasketballMatch.setAwayTeamId(((Number) ml.get("away_team_key")).longValue());
        allSportsBasketballMatch.setMatchTime((String) ml.get("event_time"));
        allSportsBasketballMatch.setHomeTeamLogo((String) ml.get("home_team_logo"));
        allSportsBasketballMatch.setAwayTeamLogo((String) ml.get("away_team_logo"));
        allSportsBasketballMatch.setHomeTeamName((String) ml.get("event_home_team"));
        allSportsBasketballMatch.setAwayTeamName((String) ml.get("event_away_team"));
        allSportsBasketballMatch.setMatchDate((String) ml.get("event_date"));
        String matchStatus = (String) ml.get("event_status");
        String eventLive = (String) ml.get("event_live");
        if (StringUtils.equals(matchStatus, "")) {
            allSportsBasketballMatch.setHomeScore(0);
            allSportsBasketballMatch.setAwayScore(0);
            allSportsBasketballMatch.setStatus("");
        } else {
            String scores = (String) ml.get("event_final_result");
            if (StringUtils.equals("", scores)) {
                allSportsBasketballMatch.setHomeScore(0);
                allSportsBasketballMatch.setAwayScore(0);
            } else {
                String[] scoreArr = scores.split("-");
                if (scoreArr.length > 0) {
                    allSportsBasketballMatch.setHomeScore(Integer.parseInt(scoreArr[0].trim()));
                    allSportsBasketballMatch.setAwayScore(Integer.parseInt(scoreArr[1].trim()));
                } else {
                    allSportsBasketballMatch.setHomeScore(0);
                    allSportsBasketballMatch.setAwayScore(0);
                }
            }
            allSportsBasketballMatch.setStatus((String) ml.get("event_status"));
        }
        // if eventLive equals 1 , is playing now
        allSportsBasketballMatch.setCompetitionName((String) ml.get("league_name"));
        allSportsBasketballMatch.setCompetitionId(((Number) ml.get("league_key")).longValue());
        allSportsBasketballMatch.setEventLive(eventLive);
        return allSportsBasketballMatch;
    }


    private void getMatchLineUp(JSONArray lineupArray, Long matchId, Integer teamType) {
        if (lineupArray != null && !lineupArray.isEmpty()) {
            for (int i = 0; i < lineupArray.size(); i++) {
                Map<String, Object> ml = (Map<String, Object>) lineupArray.get(i);
                AllSportsBasketballLineUp allSportsBasketballLineUp = getAllSportsLineUp(ml, matchId, teamType);
                int exist = allSportsBasketballLineUpDao.queryExist(allSportsBasketballLineUp.getPlayerId(), matchId);
                if (exist <= 0) {
                    allSportsBasketballLineUpDao.insertData(allSportsBasketballLineUp);
                } else {
                    allSportsBasketballLineUpDao.updateData(allSportsBasketballLineUp);
                }
            }
        }
    }

    private AllSportsBasketballLineUp getAllSportsLineUp(Map<String, Object> ml, Long matchId, Integer teamType) {
        AllSportsBasketballLineUp allSportsBasketballLineUp = new AllSportsBasketballLineUp();
        Number playerId = (Number) ml.get("player_id");
        String playerName = (String) ml.get("player");
        /*出场时间*/
        String minutes = (String) ml.get("player_minutes");
        /*得分*/
        String point = (String) ml.get("player_points");
        /*助攻*/
        String assists = (String) ml.get("player_assists");
        /*抢断*/
        String steals = (String) ml.get("player_steals");
        /*篮板 */
        String totalRebounds = (String) ml.get("player_total_rebounds");
        /*罚球 总数*/
        String freeThrowsGoalsAttempts = (String) ml.get("player_freethrows_goals_attempts");
        /*罚球命中*/
        String freeThrowsGoalsMade = (String) ml.get("player_freethrows_goals_made");
        /*犯规*/
        String personalFouls = (String) ml.get("player_personal_fouls");
        /*失误*/
        String turnovers = (String) ml.get("player_turnovers");
        /*三分 总数*/
        String threePointGoalsAttempts = (String) ml.get("player_threepoint_goals_attempts");
        /*三分命中*/
        String threePointGoalsMade = (String) ml.get("player_threepoint_goals_made");
        /*盖帽*/
        String blocks = (String) ml.get("player_blocks");
        /*投篮总数*/
        String fieldGoalsAttempts = (String) ml.get("player_field_goals_attempts");
        /*投篮命中*/
        String fieldGoalsMade = (String) ml.get("player_field_goals_made");
        allSportsBasketballLineUp.setPlayerId(playerId.longValue());
        allSportsBasketballLineUp.setMatchId(matchId);
        allSportsBasketballLineUp.setType(teamType);
        allSportsBasketballLineUp.setPlayerName(playerName);
        allSportsBasketballLineUp.setBlocks(blocks);
        allSportsBasketballLineUp.setMinutes(minutes);
        allSportsBasketballLineUp.setAssists(assists);
        allSportsBasketballLineUp.setTurnovers(turnovers);
        allSportsBasketballLineUp.setPoint(point);
        allSportsBasketballLineUp.setSteals(steals);
        allSportsBasketballLineUp.setFieldGoalsAttempts(fieldGoalsAttempts);
        allSportsBasketballLineUp.setFieldGoalsMade(fieldGoalsMade);
        allSportsBasketballLineUp.setPersonalFouls(personalFouls);
        allSportsBasketballLineUp.setThreePointGoalsAttempts(threePointGoalsAttempts);
        allSportsBasketballLineUp.setThreePointGoalsMade(threePointGoalsMade);
        allSportsBasketballLineUp.setFreeThrowsGoalsAttempts( freeThrowsGoalsAttempts);
        allSportsBasketballLineUp.setFreeThrowsGoalsMade(freeThrowsGoalsMade);
        allSportsBasketballLineUp.setTotalRebounds(totalRebounds);
        return allSportsBasketballLineUp;
    }

}
