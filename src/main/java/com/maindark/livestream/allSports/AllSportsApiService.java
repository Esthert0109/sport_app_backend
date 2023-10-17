package com.maindark.livestream.allSports;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.*;
import com.maindark.livestream.enums.IsFirst;
import com.maindark.livestream.enums.TeamEnum;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.util.StreamToListUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class AllSportsApiService {

    @Resource
    AllSportsConfig allSportsConfig;
    @Resource
    AllSportsFootballCompetitionDao allSportsFootballCompetitionDao;

    @Resource
    AllSportsFootballTeamDao allSportsFootballTeamDao;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    @Resource
    AllSportsAwayMatchLineUpDao allSportsAwayMatchLineUpDao;
    @Resource
    AllSportsHomeMatchLineUpDao allSportsHomeMatchLineUpDao;

    @Resource
    AllSportsFootballLiveDataDao allSportsFootballLiveDataDao;

    public Map<String, Object> getAllLeagues() {
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLeagues());
        String result = HttpUtil.getNaMiData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            List<Map<String, Object>> competitionlist = (List<Map<String, Object>>) resultObj.get("result");
            if (competitionlist != null && !competitionlist.isEmpty()) {
                competitionlist.stream().forEach(ml -> {
                    AllSportsFootballCompetition competition = getAllSportsFootballCompetition(ml);
                    AllSportsFootballCompetition competitionFromDb = allSportsFootballCompetitionDao.getAllSportsFootballCompetitionById(competition.getId());
                    if (competitionFromDb == null) {
                        allSportsFootballCompetitionDao.insert(competition);
                        getAllTeams(String.valueOf(ml.get("league_key")));
                    }
                });
            }
        }
        return resultObj;
    }

    public Map<String, Object> getAllTeams(String leagueId) {
        String teamUrl = allSportsConfig.getAllSportsApi(allSportsConfig.getTeams());
        teamUrl = teamUrl.replace("{}", leagueId);
        String result = HttpUtil.getNaMiData(teamUrl);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            List<Map<String, Object>> teamslist = (List<Map<String, Object>>) resultObj.get("result");
            if (teamslist != null && !teamslist.isEmpty()) {
                teamslist.stream().forEach(ml -> {
                    AllSportsFootballTeam team = getAllSportsFootballTeam(ml, leagueId);
                    AllSportsFootballTeam teamFromDb = allSportsFootballTeamDao.getAllSportsTeamById(team.getId());
                    if (teamFromDb == null) {
                        allSportsFootballTeamDao.insert(team);
                    }
                });
            }
        }
        return resultObj;
    }


    public Map<String, Object> getAllFixtures(String from, String to) {
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures()) + "&from=" + from + "&to=" + to;
        List<AllSportsFootballMatch> allSportsFootballMatches = getAllSportsMatch(url);
        if (allSportsFootballMatches != null) {
            allSportsFootballMatches.forEach(ml -> {
                int exist = allSportsFootballMatchDao.queryMatchIsExists(ml.getId());
                if (exist <= 0) {
                    allSportsFootballMatchDao.insert(ml);
                }
            });
        }
        return null;
    }

    public Map<String, Object> getLiveMatchByMatchId(String matchId) {
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixtures()) + "&matchId=" + matchId;
        String result = HttpUtil.getNaMiData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer) resultObj.get("success");
            if (1 == success) {
                List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                if (matches != null && !matches.isEmpty()) {
                    int size = matches.size();
                    for (int i = 0; i < size; i++) {
                        Map<String, Object> ml = matches.get(i);
                        String eventLive = (String) ml.get("event_live");
                        if (StringUtils.equals("0", eventLive)) {
                            AllSportsFootballMatch allSportsFootballMatch = getAllSportsMatchByMatchId(ml);
                            allSportsFootballMatchDao.updateAllSportsMatch(allSportsFootballMatch);
                            Map<String, Object> lineups = (Map<String, Object>) ml.get("lineups");
                            //Number matchId = (Number) ml.get("event_key");
                            Number homeTeamId = (Number) ml.get("home_team_key");
                            Number awayTeamId = (Number) ml.get("away_team_key");
                            if (lineups != null && !lineups.isEmpty()) {
                                // set home team line-up
                                Map<String, Object> homeTeam = (Map<String, Object>) lineups.get("home_team");
                                if (homeTeam != null && !homeTeam.isEmpty()) {
                                    JSONArray startingLineups = (JSONArray) homeTeam.get("starting_lineups");
                                    JSONArray substitutes = (JSONArray) homeTeam.get("substitutes");
                                    getMatchLineUp(startingLineups, Long.valueOf(matchId), substitutes, homeTeamId.longValue(), TeamEnum.HOME.getCode());
                                }
                                // set away team line-up
                                Map<String, Object> awayTeam = (Map<String, Object>) lineups.get("away_team");
                                if (awayTeam != null && !awayTeam.isEmpty()) {
                                    JSONArray startingLineups = (JSONArray) awayTeam.get("starting_lineups");
                                    JSONArray substitutes = (JSONArray) awayTeam.get("substitutes");
                                    getMatchLineUp(startingLineups, Long.valueOf(matchId), substitutes, awayTeamId.longValue(),TeamEnum.AWAY.getCode());
                                }
                            }
                        }
                    }
                }
            }
        }
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

    /**
     *  set match line-up
     *
     */
    private void getMatchLineUp(JSONArray startingLineups,Long matchId,JSONArray substitutes,Long teamId,String teamType){
        setAllSportsFootballMatchLineUp(startingLineups, matchId, teamId, IsFirst.YES.getCode(),teamType);
        setAllSportsFootballMatchLineUp(substitutes, matchId, teamId,IsFirst.NO.getCode(),teamType);
    }



    /**
     * first 0 no 1 yes
     * teamType 0 homeTam 1 awayTeam
     */
    private void setAllSportsFootballMatchLineUp(JSONArray jsonArray, Long matchId,Long teamId,Integer first,String teamType) {
        if(jsonArray != null){
            int size = jsonArray.size();
            for(int i=0;i<size;i++){
                Map<String,Object> map = (Map<String,Object>)jsonArray.get(i);
                String playerName = (String)map.get("player");
                Integer playerNumber = (Integer)map.get("player_number");
                Integer playerPosition = (Integer)map.get("player_position");
                Number playerKey = (Number)map.get("player_key");
                if(StringUtils.equals("0",teamType)){
                    int exist = allSportsHomeMatchLineUpDao.queryExists(playerKey.longValue());
                    if(exist <=0){
                        BaseFootballMatchLineup allSportsHomeMatchLineUp = getAllSportsLineUp(playerKey.longValue(),playerNumber,playerPosition,matchId,teamId,playerName,first,TeamEnum.HOME.getCode());
                        allSportsHomeMatchLineUpDao.insert((AllSportsHomeMatchLineUp) allSportsHomeMatchLineUp);
                    }
                } else {
                    int exist = allSportsAwayMatchLineUpDao.queryExists(playerKey.longValue());
                    if(exist <=0){
                        BaseFootballMatchLineup allSportsAwayMatchLineUp = getAllSportsLineUp(playerKey.longValue(),playerNumber,playerPosition,matchId,teamId,playerName,first,TeamEnum.AWAY.getCode());
                        allSportsAwayMatchLineUpDao.insert((AllSportsAwayMatchLineUp) allSportsAwayMatchLineUp);
                    }
                }

            }
        }
    }


    private BaseFootballMatchLineup getAllSportsLineUp(Long playerId,Integer playNumber,Integer playPosition,Long matchId,Long teamId,String playerName,Integer first,String teamType) {
        BaseFootballMatchLineup  allSportsHomeMatchLineUp = null;
        if(StringUtils.equals("0",teamType)){
            allSportsHomeMatchLineUp = new AllSportsHomeMatchLineUp();
        } else {
            allSportsHomeMatchLineUp = new AllSportsAwayMatchLineUp();
        }
        allSportsHomeMatchLineUp.setId(playerId);
        allSportsHomeMatchLineUp.setMatchId(matchId);
        allSportsHomeMatchLineUp.setTeamId(teamId);
        allSportsHomeMatchLineUp.setShirtNumber(playNumber);
        allSportsHomeMatchLineUp.setPosition(playPosition);
        allSportsHomeMatchLineUp.setPlayerName(playerName);
        allSportsHomeMatchLineUp.setFirst(first);
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getPlayers()).replace("{}",String.valueOf(playerId));
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer) resultObj.get("success");
            if(1 == success){
                JSONArray playArray = (JSONArray)resultObj.get("result");
                Map<String,Object> playMap = (Map<String, Object>) playArray.get(0);
                String captain = (String)playMap.get("player_is_captain");
                if(StringUtils.isBlank(captain)){
                    allSportsHomeMatchLineUp.setCaptain(0);
                } else {
                    allSportsHomeMatchLineUp.setCaptain(1);
                }
                String playerImage = (String)playMap.get("player_image");
                String playerRating = (String)playMap.get("player_rating");
                allSportsHomeMatchLineUp.setPlayerLogo(playerImage);
                allSportsHomeMatchLineUp.setRating(playerRating);
            }
        }
        return allSportsHomeMatchLineUp;
    }

    private AllSportsFootballMatch getAllSportsMatchByMatchId(Map<String,Object> ml) {
        AllSportsFootballMatch allSportsFootballMatch = new AllSportsFootballMatch();
        Number eventKey = (Number) ml.get("event_key");
        allSportsFootballMatch.setId(eventKey.longValue());
        allSportsFootballMatch.setHomeTeamId(((Number) ml.get("home_team_key")).longValue());
        allSportsFootballMatch.setAwayTeamId(((Number) ml.get("away_team_key")).longValue());
        allSportsFootballMatch.setMatchTime((String) ml.get("event_time"));
        allSportsFootballMatch.setHomeTeamLogo((String) ml.get("home_team_logo"));
        allSportsFootballMatch.setAwayTeamLogo((String) ml.get("away_team_logo"));
        allSportsFootballMatch.setHomeTeamName((String) ml.get("event_home_team"));
        allSportsFootballMatch.setAwayTeamName((String) ml.get("event_away_team"));
        allSportsFootballMatch.setMatchDate((String) ml.get("event_date"));
        String matchStatus = (String) ml.get("event_status");
        String eventLive = (String) ml.get("event_live");
        if (StringUtils.equals(matchStatus, "")) {
            allSportsFootballMatch.setHomeTeamScore(0);
            allSportsFootballMatch.setAwayTeamScore(0);
            allSportsFootballMatch.setStatus("");
        } else {
            String scores = (String) ml.get("event_final_result");
            if (StringUtils.equals("", scores)) {
                allSportsFootballMatch.setHomeTeamScore(0);
                allSportsFootballMatch.setAwayTeamScore(0);
            } else {
                String[] scoreArr = scores.split("-");
                if (scoreArr != null && scoreArr.length > 0) {
                    allSportsFootballMatch.setHomeTeamScore(Integer.parseInt(scoreArr[0].trim()));
                    allSportsFootballMatch.setAwayTeamScore(Integer.parseInt(scoreArr[1].trim()));
                } else {
                    allSportsFootballMatch.setHomeTeamScore(0);
                    allSportsFootballMatch.setAwayTeamScore(0);
                }
            }
            allSportsFootballMatch.setStatus((String) ml.get("event_status"));
        }
        // if eventLive equals 1 , is playing now
        if (StringUtils.equals("0", eventLive)) {
            allSportsFootballMatch.setLineUp(0);
        } else {
            Map<String, Object> lineups = (Map<String, Object>) ml.get("lineups");
            if (lineups != null && !lineups.isEmpty()) {
                allSportsFootballMatch.setLineUp(1);
            }
        }
        allSportsFootballMatch.setVenueName((String) ml.get("event_stadium"));
        allSportsFootballMatch.setRefereeName((String) ml.get("event_referee"));
        allSportsFootballMatch.setCompetitionName((String) ml.get("league_name"));
        allSportsFootballMatch.setHomeFormation((String) ml.get("event_home_formation"));
        allSportsFootballMatch.setAwayFormation((String) ml.get("event_away_formation"));
        allSportsFootballMatch.setCompetitionId(((Number) ml.get("league_key")).longValue());
        allSportsFootballMatch.setEventLive(eventLive);
        return allSportsFootballMatch;
    }


    public Map<String, Object> getLiveData(String matchId) {
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLivescore()) + "&matchId=" + matchId;
        String result = HttpUtil.getNaMiData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer) resultObj.get("success");
            if (1 == success) {
                List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                if (matches != null && !matches.isEmpty()) {
                    matches.stream().forEach(ml ->{
                        String eventLive = (String)ml.get("event_live");
                        if(StringUtils.equals("1",eventLive)) {
                            //Number matchId = (Number) ml.get("event_key");
                            int exist = allSportsFootballLiveDataDao.queryExist(Long.valueOf(matchId));
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
        return null;
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
        // get home and away team score
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
