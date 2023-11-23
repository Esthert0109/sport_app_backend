package com.maindark.livestream.allSports;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.AllSportsBasketballMatchDao;
import com.maindark.livestream.domain.AllSportsBasketballMatch;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.util.StreamToListUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class AllSportsApiBasketballService {
    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

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
                        allSportsBasketballMatch.setHomeTeamLogo((String)ml.get("event_home_team_logo"));
                        allSportsBasketballMatch.setAwayTeamLogo((String)ml.get("event_away_team_logo"));
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

}
