package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsFootballCompetitionDao;
import com.maindark.livestream.dao.AllSportsFootballTeamDao;
import com.maindark.livestream.domain.AllSportsFootballCompetition;
import com.maindark.livestream.domain.AllSportsFootballTeam;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AllSportsService {

    @Resource
    AllSportsConfig allSportsConfig;
    @Resource
    AllSportsFootballCompetitionDao allSportsFootballCompetitionDao;

    @Resource
    AllSportsFootballTeamDao allSportsFootballTeamDao;

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
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixturesLeagueId()) +"&from="+ from + "&to=" + to;
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        return resultObj;
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

}
