package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsFootballCompetitionDao;
import com.maindark.livestream.dao.AllSportsFootballTeamDao;
import com.maindark.livestream.domain.AwayMatchLineUp;
import com.maindark.livestream.domain.HomeMatchLineUp;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.FootballListKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.vo.FootballMatchLineUpVo;
import com.maindark.livestream.vo.FootballMatchLiveDataVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class AllSportsApiService {

    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsFootballCompetitionDao allSportsFootballCompetitionDao;
    @Resource
    AllSportsFootballTeamDao allSportsFootballTeamDao;

    @Resource
    RedisService redisService;

    public List<FootballMatchVo> getFootBallMatchList(String competitionName, String teamName) {
        if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
            throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
        }
        List<FootballMatchVo> list;
        Integer leagueId ;
        Integer teamId ;
        String url = "";
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        if (!StringUtils.isBlank(competitionName)){
            leagueId = allSportsFootballCompetitionDao.getAllSportsFootballCompetitionByName(competitionName.trim());
            url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixturesLeagueId()).replace("{}",String.valueOf(leagueId))+"&from="+ from + "&to=" + to;
        } else if (!StringUtils.isBlank(teamName)) {
            teamId = allSportsFootballTeamDao.getAllSportsTeamByName(teamName.trim());
            url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixturesTeamId()).replace("{}",String.valueOf(teamId))+"&from="+ from + "&to=" + to;
        }
        list = getFootballMatchVos(url);
        return list;
    }

    public Map<String, List<FootballMatchVo>> getFootballMatchesInSevenDays() {
        LocalDate now = LocalDate.now();
        String nowSeconds = DateUtil.convertDateToStr(now);
        Map<String,List<FootballMatchVo>> results = redisService.get(FootballListKey.listKey,nowSeconds,Map.class);
        if (results == null) {
            LocalDate tomorrow = now.plusDays(1);
            LocalDate future = now.plusDays(6);
            LocalDate past = now.minusDays(6);
            String tomorrowS = DateUtil.convertDateToStr(tomorrow);
            String futureSeconds = DateUtil.convertDateToStr(future);
            String pastSeconds = DateUtil.convertDateToStr(past);
            String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures())+"&from="+ pastSeconds + "&to=" + nowSeconds;
            List<FootballMatchVo> pastMatches = getFootballMatchVos(url);
            url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures())+"&from="+ nowSeconds + "&to=" + tomorrowS;
            List<FootballMatchVo> startMatches = getFootballMatchVos(url);
            url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures())+"&from="+ nowSeconds + "&to=" + futureSeconds;
            List<FootballMatchVo> futureMatches = getFootballMatchVos(url);
            results = new HashMap<>();
            results.put("pass",pastMatches);
            results.put("start",startMatches);
            results.put("future",futureMatches);
            redisService.set(FootballListKey.listKey,nowSeconds,results);
        }
        return results;
    }

    public List<FootballMatchVo> getMatchListByDate(String date) {
        String expectedDate = DateUtil.convertDateToStr(DateUtil.convertStringToDate(date));
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures())+"&from="+ expectedDate + "&to=" + expectedDate;
        return getFootballMatchVos(url);
    }

    public FootballMatchLineUpVo getFootballMatchLineUpByMatchId(String matchId) {
        //String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLivescore()) + "&matchId="+ matchId;
        String url = "https://apiv2.allsportsapi.com/football/?met=Fixtures&APIkey=1012e61cdb2ef9391199956c0c36d6f611378a4e4ab02a1cb13816508684f0c5&from=2023-10-12&to=2023-10-12&matchId=1183466";
        getFootballMatchLineUp(url);
        return null;
    }

    public FootballMatchLiveDataVo getMatchLiveData(Integer matchId) {
        return null;
    }



    private List<FootballMatchVo> getFootballMatchVos(String url){
        List<FootballMatchVo> list = null;
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()){
            int success = (Integer)resultObj.get("success");
            if(1 == success) {
                List<Map<String,Object>> matchesList = (List<Map<String,Object>>)resultObj.get("result");
                Stream<FootballMatchVo> footballMatchVoStream = matchesList.stream().map(ml ->{
                    FootballMatchVo footballMatchVo = new FootballMatchVo();
                    footballMatchVo.setId((Integer)ml.get("event_key"));
                    footballMatchVo.setMatchTimeStr((String)ml.get("event_time"));
                    footballMatchVo.setHomeTeamLogo((String)ml.get("home_team_logo"));
                    footballMatchVo.setAwayTeamLogo((String)ml.get("away_team_logo"));
                    footballMatchVo.setHomeTeamName((String)ml.get("event_home_team"));
                    footballMatchVo.setAwayTeamName((String)ml.get("event_away_team"));
                    String matchStatus = (String)ml.get("event_status");
                    String eventLive = (String)ml.get("event_live");
                    if(StringUtils.equals(matchStatus,"")){
                        footballMatchVo.setHomeTeamScore(0);
                        footballMatchVo.setAwayTeamScore(0);
                        footballMatchVo.setStatusStr("");
                    } else {
                        String scores = (String)ml.get("event_final_result");
                        if(StringUtils.equals("",scores)){
                            footballMatchVo.setHomeTeamScore(0);
                            footballMatchVo.setAwayTeamScore(0);
                        } else {
                            String[] scoreArr = scores.split("-");
                            footballMatchVo.setHomeTeamScore(Integer.parseInt(scoreArr[0].trim()));
                            footballMatchVo.setAwayTeamScore(Integer.parseInt(scoreArr[1].trim()));
                        }
                        footballMatchVo.setStatusStr((String)ml.get("event_status"));
                    }
                    // if eventLive equals 1 , is playing now
                    if(StringUtils.equals("0",eventLive)){
                        footballMatchVo.setLineUp(0);
                    } else {
                        footballMatchVo.setLineUp(1);
                    }
                    footballMatchVo.setVenueName((String)ml.get("event_stadium"));
                    footballMatchVo.setRefereeName((String)ml.get("event_referee"));
                    footballMatchVo.setCompetitionName((String)ml.get("league_name"));
                    footballMatchVo.setCompetitionId((Integer)ml.get("league_key"));
                    return footballMatchVo;
                });
                list = getArrayListFromStream(footballMatchVoStream);
            }
        }
        return list;
    }


    private FootballMatchLineUpVo getFootballMatchLineUp(String url){
        FootballMatchLineUpVo footballMatchLineUpVo = new FootballMatchLineUpVo();
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer) resultObj.get("success");
            if(1 == success){
                List<Map<String,Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                if(matches != null && !matches.isEmpty()){
                    Map<String,Object> match = matches.get(0);
                    Map<String,Object> lineups = (Map<String, Object>) match.get("lineups");
                    if(lineups != null && !lineups.isEmpty()) {
                        Map<String,Object> homeTeam = (Map<String,Object>)lineups.get("home_team");
                        JSONArray startingLineups = (JSONArray) homeTeam.get("starting_lineups");
                        JSONArray substitutes = (JSONArray) homeTeam.get("substitutes");
                        getHomeMatchLineUp(startingLineups);
                    }
                }
            }
        }
        return null;
    }

    private List<HomeMatchLineUp> getHomeMatchLineUp(JSONArray jsonArray){
        if(jsonArray != null){
            int size = jsonArray.size();
            for(int i=0;i<size;i++){
                Map<String,Object> map = (Map<String,Object>)jsonArray.get(i);
                String playerName = (String)map.get("player");
                Integer playerNumber = (Integer)map.get("player_number");
                Integer playerPosition = (Integer)map.get("player_position");
                Number playerKey = (Number)map.get("player_key");
                playerKey.toString();

            }
        }
        return null;
    }
    private List<AwayMatchLineUp> getAwayMatchLineUp(){
        return null;
    }


    public static <T> ArrayList<T>
    getArrayListFromStream(Stream<T> stream)
    {
        List<T> list = stream.toList();
        // Create an ArrayList of the List
        //ArrayList<T> arrayList = new ArrayList<T>(list);
        // Return the ArrayList
        return new ArrayList<T>(list);
    }


}
