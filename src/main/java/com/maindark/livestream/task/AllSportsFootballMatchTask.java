package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsFootballMatchDao;
import com.maindark.livestream.domain.AllSportsFootballMatch;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.util.StreamToListUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Slf4j
@EnableScheduling
public class AllSportsFootballMatchTask {
    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    @Scheduled(cron = "0 0 0 * * ?")
    public void getFootballMatch(){
        LocalDate localDate = LocalDate.now();
        String from = DateUtil.convertLocalDateToStr(localDate);
        String to = DateUtil.convertLocalDateToStr(localDate);
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures()) + "&from=" + from + "&to=" + to;
        List<AllSportsFootballMatch> allSportsFootballMatches = getAllSportsMatch(url);
        if (allSportsFootballMatches != null) {
            allSportsFootballMatches.forEach(ml -> {
                int exist = allSportsFootballMatchDao.queryMatchIsExists(ml.getId());
                if (exist <= 0) {
                    allSportsFootballMatchDao.insert(ml);
                } else {
                    allSportsFootballMatchDao.updateAllSportsMatch(ml);
                }
            });
        }
    }


    private List<AllSportsFootballMatch> getAllSportsMatch(String url){
        List<AllSportsFootballMatch> list = null;
        String result = HttpUtil.getAllSportsData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer)resultObj.get("success");
            if(1 == success) {
                List<Map<String,Object>> matchesList = (List<Map<String,Object>>)resultObj.get("result");
                if(matchesList != null && !matchesList.isEmpty()){
                    Stream<AllSportsFootballMatch> footballMatchVoStream = matchesList.stream().map(ml ->{
                        AllSportsFootballMatch allSportsFootballMatch = new AllSportsFootballMatch();
                        Number eventKey = (Number)ml.get("event_key");
                        allSportsFootballMatch.setId(eventKey.longValue());
                        allSportsFootballMatch.setHomeTeamId(((Number)ml.get("home_team_key")).longValue());
                        allSportsFootballMatch.setAwayTeamId(((Number)ml.get("away_team_key")).longValue());
                        allSportsFootballMatch.setMatchTime((String)ml.get("event_time"));
                        allSportsFootballMatch.setHomeTeamLogo((String)ml.get("home_team_logo"));
                        allSportsFootballMatch.setAwayTeamLogo((String)ml.get("away_team_logo"));
                        allSportsFootballMatch.setHomeTeamName((String)ml.get("event_home_team"));
                        allSportsFootballMatch.setAwayTeamName((String)ml.get("event_away_team"));
                        allSportsFootballMatch.setMatchDate((String)ml.get("event_date"));
                        String matchStatus = (String)ml.get("event_status");
                        String eventLive = (String)ml.get("event_live");
                        if(StringUtils.equals(matchStatus,"")){
                            allSportsFootballMatch.setHomeTeamScore(0);
                            allSportsFootballMatch.setAwayTeamScore(0);
                            allSportsFootballMatch.setStatus("");
                        } else {
                            String scores = (String)ml.get("event_final_result");
                            if(StringUtils.equals("",scores)){
                                allSportsFootballMatch.setHomeTeamScore(0);
                                allSportsFootballMatch.setAwayTeamScore(0);
                            } else {
                                String[] scoreArr = scores.split("-");
                                if(scoreArr != null && scoreArr.length >0) {
                                    allSportsFootballMatch.setHomeTeamScore(Integer.parseInt(scoreArr[0].trim()));
                                    allSportsFootballMatch.setAwayTeamScore(Integer.parseInt(scoreArr[1].trim()));
                                } else {
                                    allSportsFootballMatch.setHomeTeamScore(0);
                                    allSportsFootballMatch.setAwayTeamScore(0);
                                }
                            }
                            allSportsFootballMatch.setStatus((String)ml.get("event_status"));
                        }
                        // if eventLive equals 1 , is playing now
                        if(StringUtils.equals("0",eventLive)){
                            Map<String,Object> lineups = (Map<String, Object>) ml.get("lineups");
                            if(lineups != null && !lineups.isEmpty()) {
                                Map<String, Object> homeTeam = (Map<String, Object>) lineups.get("home_team");
                                if (homeTeam != null && !homeTeam.isEmpty()) {
                                    JSONArray startingLineups = (JSONArray) homeTeam.get("starting_lineups");
                                    if(startingLineups != null && !startingLineups.isEmpty()){
                                        allSportsFootballMatch.setLineUp(1);
                                    }
                                } else {
                                    allSportsFootballMatch.setLineUp(0);
                                }
                            }
                        } else {
                            Map<String,Object> lineups = (Map<String, Object>) ml.get("lineups");
                            if(lineups != null && !lineups.isEmpty()) {
                                Map<String, Object> homeTeam = (Map<String, Object>) lineups.get("home_team");
                                if (homeTeam != null && !homeTeam.isEmpty()) {
                                    JSONArray startingLineups = (JSONArray) homeTeam.get("starting_lineups");
                                    if(startingLineups != null && !startingLineups.isEmpty()){
                                        allSportsFootballMatch.setLineUp(1);
                                    }
                                }
                            }
                        }
                        allSportsFootballMatch.setVenueName((String)ml.get("event_stadium"));
                        allSportsFootballMatch.setRefereeName((String)ml.get("event_referee"));
                        allSportsFootballMatch.setCompetitionName((String)ml.get("league_name"));
                        allSportsFootballMatch.setHomeFormation((String)ml.get("event_home_formation"));
                        allSportsFootballMatch.setAwayFormation((String)ml.get("event_away_formation"));
                        allSportsFootballMatch.setCompetitionId(((Number)ml.get("league_key")).longValue());
                        allSportsFootballMatch.setEventLive(eventLive);
                        return allSportsFootballMatch;
                    });
                    list = StreamToListUtil.getArrayListFromStream(footballMatchVoStream);
                }

            }
        }
        return list;
    }
}
