package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsFootballCompetitionDao;
import com.maindark.livestream.dao.AllSportsFootballMatchDao;
import com.maindark.livestream.dao.AllSportsFootballTeamDao;
import com.maindark.livestream.domain.AllSportsFootballCompetition;
import com.maindark.livestream.domain.AllSportsFootballMatch;
import com.maindark.livestream.domain.AllSportsFootballTeam;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class AllSportsService {

    @Resource
    AllSportsConfig allSportsConfig;
    @Resource
    AllSportsFootballCompetitionDao allSportsFootballCompetitionDao;

    @Resource
    AllSportsFootballTeamDao allSportsFootballTeamDao;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    public Map<String,Object> getAllLeagues(){
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLeagues());
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> competitionlist = (List<Map<String, Object>>) resultObj.get("result");
            if(competitionlist != null && !competitionlist.isEmpty()) {
                competitionlist.stream().forEach(ml ->{
                    AllSportsFootballCompetition competition = getAllSportsFootballCompetition(ml);
                    AllSportsFootballCompetition competitionFromDb = allSportsFootballCompetitionDao.getAllSportsFootballCompetitionById(competition.getId());
                    if(competitionFromDb == null){
                        allSportsFootballCompetitionDao.insert(competition);
                        getAllTeams(String.valueOf(ml.get("league_key")));
                    }
                });
            }
        }
        return resultObj;
    }



    public Map<String,Object> getAllTeams(String leagueId){
        String teamUrl = allSportsConfig.getAllSportsApi(allSportsConfig.getTeams());
        teamUrl = teamUrl.replace("{}",leagueId);
        String result = HttpUtil.getNaMiData(teamUrl);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> teamslist = (List<Map<String, Object>>) resultObj.get("result");
            if(teamslist != null && !teamslist.isEmpty()) {
                teamslist.stream().forEach(ml ->{
                    AllSportsFootballTeam team = getAllSportsFootballTeam(ml,leagueId);
                    AllSportsFootballTeam teamFromDb = allSportsFootballTeamDao.getAllSportsTeamById(team.getId());
                    if(teamFromDb == null){
                        allSportsFootballTeamDao.insert(team);
                    }
                });
            }
        }
        return resultObj;
    }

    private AllSportsFootballTeam getAllSportsFootballTeam(Map<String, Object> ml,String leagueId) {
        AllSportsFootballTeam allSportsFootballTeam = new AllSportsFootballTeam();
        Integer teamId = (Integer)ml.get("team_key");
        String teamName = (String)ml.get("team_name");
        String teamLogo = (String)ml.get("team_logo");
        JSONArray coaches = (JSONArray) ml.get("coaches");

        if(coaches != null && !coaches.isEmpty()){
            JSONObject jsonObject = (JSONObject) coaches.get(0);
            allSportsFootballTeam.setCoachName((String)jsonObject.get("coach_name"));
        }
        allSportsFootballTeam.setId(teamId);
        allSportsFootballTeam.setTeamName(teamName);
        allSportsFootballTeam.setTeamLogo(teamLogo);
        allSportsFootballTeam.setCompetitionId(Integer.valueOf(leagueId));
        return allSportsFootballTeam;
    }

    public Map<String,Object> getAllFixtures(String from,String to){
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures()) +"&from="+ from + "&to=" + to;
        List<AllSportsFootballMatch> allSportsFootballMatches = getAllSportsMatch(url);
        if(allSportsFootballMatches != null) {
            allSportsFootballMatches.forEach(ml ->{
                int exist = allSportsFootballMatchDao.queryMatchIsExists(ml.getId());
                if(exist <=0) {
                    allSportsFootballMatchDao.insert(ml);
                }
            });
        }
        return null;
    }

    private AllSportsFootballCompetition getAllSportsFootballCompetition(Map<String, Object> ml) {
        Integer id = (Integer)ml.get("league_key");
        String leagueName = (String)ml.get("league_name");
        String countryName = (String)ml.get("country_name");
        String leagueLogo = (String)ml.get("league_logo");
        String countryLogo = (String)ml.get("country_logo");
        AllSportsFootballCompetition allSportsFootballCompetition = new AllSportsFootballCompetition();
        allSportsFootballCompetition.setId(id);
        allSportsFootballCompetition.setCompetitionName(leagueName);
        allSportsFootballCompetition.setCountry(countryName);
        allSportsFootballCompetition.setLogo(leagueLogo);
        allSportsFootballCompetition.setCountryLogo(countryLogo);
        return allSportsFootballCompetition;
    }

    private List<AllSportsFootballMatch> getAllSportsMatch(String url){
        List<AllSportsFootballMatch> list = null;
        String result = HttpUtil.getNaMiData(url);
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
                            allSportsFootballMatch.setLineUp(0);
                        } else {
                            Map<String,Object> lineups = (Map<String, Object>) ml.get("lineups");
                            if(lineups != null && !lineups.isEmpty()) {
                                allSportsFootballMatch.setLineUp(1);
                            }
                        }
                        allSportsFootballMatch.setVenueName((String)ml.get("event_stadium"));
                        allSportsFootballMatch.setRefereeName((String)ml.get("event_referee"));
                        allSportsFootballMatch.setCompetitionName((String)ml.get("league_name"));
                        allSportsFootballMatch.setCompetitionId((Integer)ml.get("league_key"));
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
