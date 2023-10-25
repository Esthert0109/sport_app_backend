package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsAwayMatchLineUpDao;
import com.maindark.livestream.dao.AllSportsFootballMatchDao;
import com.maindark.livestream.dao.AllSportsHomeMatchLineUpDao;
import com.maindark.livestream.domain.AllSportsAwayMatchLineUp;
import com.maindark.livestream.domain.AllSportsFootballMatch;
import com.maindark.livestream.domain.AllSportsHomeMatchLineUp;
import com.maindark.livestream.domain.BaseFootballMatchLineup;
import com.maindark.livestream.enums.IsFirst;
import com.maindark.livestream.enums.TeamEnum;
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
public class AllSportsFootballTask {

    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    @Resource
    AllSportsAwayMatchLineUpDao allSportsAwayMatchLineUpDao;
   @Resource
   AllSportsHomeMatchLineUpDao allSportsHomeMatchLineUpDao;


    @Scheduled(cron = "0 */2 * * * ? ")
    public void getAllSportsFootballMatchLineUp(){
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLivescore());
        String result = HttpUtil.getAllSportsData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if(success != null){
                if(1 == success){
                    List<Map<String,Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                    if(matches != null && !matches.isEmpty()){
                        int size = matches.size();
                        for(int i=0;i<size;i++){
                            Map<String,Object> ml = matches.get(i);
                            String eventLive = (String)ml.get("event_live");
                            if(StringUtils.equals("1",eventLive)){
                                AllSportsFootballMatch allSportsFootballMatch = getAllSportsMatch(ml);
                                int exists = allSportsFootballMatchDao.queryMatchIsExists(allSportsFootballMatch.getId());
                                if(exists <= 0){
                                    allSportsFootballMatchDao.insert(allSportsFootballMatch);
                                } else {
                                    allSportsFootballMatchDao.updateAllSportsMatch(allSportsFootballMatch);
                                }
                                Map<String,Object> lineups = (Map<String, Object>) ml.get("lineups");
                                Number matchId = (Number) ml.get("event_key");
                                Number homeTeamId = (Number) ml.get("home_team_key");
                                Number awayTeamId = (Number) ml.get("away_team_key");
                                if (lineups != null && !lineups.isEmpty()) {
                                    // set home team line-up
                                    Map<String, Object> homeTeam = (Map<String, Object>) lineups.get("home_team");
                                    if (homeTeam != null && !homeTeam.isEmpty()) {
                                        JSONArray startingLineups = (JSONArray) homeTeam.get("starting_lineups");
                                        JSONArray substitutes = (JSONArray) homeTeam.get("substitutes");
                                        getMatchLineUp(startingLineups, matchId.longValue(), substitutes, homeTeamId.longValue(), TeamEnum.HOME.getCode());
                                    }
                                    // set away team line-up
                                    Map<String, Object> awayTeam = (Map<String, Object>) lineups.get("away_team");
                                    if (awayTeam != null && !awayTeam.isEmpty()) {
                                        JSONArray startingLineups = (JSONArray) awayTeam.get("starting_lineups");
                                        JSONArray substitutes = (JSONArray) awayTeam.get("substitutes");
                                        getMatchLineUp(startingLineups, matchId.longValue(), substitutes, awayTeamId.longValue(),TeamEnum.AWAY.getCode());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    log.info("there is no live data now!");
                }
            }
        }
    }



    /**
     *  set match line-up
     *
     */
    private void getMatchLineUp(JSONArray startingLineups,Long matchId,JSONArray substitutes,Long teamId,String teamType){
        setAllSportsFootballMatchLineUp(startingLineups, matchId, teamId, IsFirst.YES.getCode(),teamType);
        setAllSportsFootballMatchLineUp(substitutes, matchId, teamId,IsFirst.NO.getCode(), teamType);
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
                    int exist = allSportsHomeMatchLineUpDao.queryExists(playerKey.longValue(),matchId);
                    if(exist <=0){
                        BaseFootballMatchLineup allSportsHomeMatchLineUp = getAllSportsLineUp(playerKey.longValue(),playerNumber,playerPosition,matchId,teamId,playerName,first,TeamEnum.HOME.getCode());
                        allSportsHomeMatchLineUpDao.insert((AllSportsHomeMatchLineUp) allSportsHomeMatchLineUp);
                    }
                } else {
                    int exist = allSportsAwayMatchLineUpDao.queryExists(playerKey.longValue(),matchId);
                    if(exist <=0){
                        BaseFootballMatchLineup allSportsAwayMatchLineUp = getAllSportsLineUp(playerKey.longValue(),playerNumber,playerPosition,matchId,teamId,playerName,first,TeamEnum.AWAY.getCode());
                        allSportsAwayMatchLineUpDao.insert((AllSportsAwayMatchLineUp) allSportsAwayMatchLineUp);
                    }
                }

            }
        }
    }


    private BaseFootballMatchLineup getAllSportsLineUp(Long playerId, Integer playNumber, Integer playPosition, Long matchId, Long teamId, String playerName, Integer first, String teamType) {
        BaseFootballMatchLineup  allSportsHomeMatchLineUp;
        if(StringUtils.equals("0",teamType)){
            allSportsHomeMatchLineUp = new AllSportsHomeMatchLineUp();
        } else {
            allSportsHomeMatchLineUp = new AllSportsAwayMatchLineUp();
        }
        allSportsHomeMatchLineUp.setPlayerId(playerId);
        allSportsHomeMatchLineUp.setMatchId(matchId);
        allSportsHomeMatchLineUp.setTeamId(teamId);
        allSportsHomeMatchLineUp.setShirtNumber(playNumber);
        allSportsHomeMatchLineUp.setPosition(playPosition);
        allSportsHomeMatchLineUp.setPlayerName(playerName);
        allSportsHomeMatchLineUp.setFirst(first);
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getPlayers()).replace("{}",String.valueOf(playerId));
        String result = HttpUtil.getAllSportsData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if(success != null){
                if(1 == success){
                    JSONArray playArray = (JSONArray)resultObj.get("result");
                    if(playArray != null && !playArray.isEmpty()){
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
            }
        }
        return allSportsHomeMatchLineUp;
    }

    private AllSportsFootballMatch getAllSportsMatch(Map<String,Object> ml) {
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
                if (scoreArr.length > 0) {
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

}
