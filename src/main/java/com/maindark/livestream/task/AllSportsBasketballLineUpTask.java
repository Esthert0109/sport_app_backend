package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsBasketballLineUpDao;
import com.maindark.livestream.dao.AllSportsBasketballMatchDao;
import com.maindark.livestream.domain.*;
import com.maindark.livestream.enums.LineUpType;
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
public class AllSportsBasketballLineUpTask {

    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

    @Resource
    AllSportsBasketballLineUpDao allSportsBasketballLineUpDao;


    //@Scheduled(cron = "0 */2 * * * ?")
    public void getAllSportsBasketballData() {
        String url = allSportsConfig.getAllSportsBasketballApi(allSportsConfig.getBasketballLiveDataUrl());
        String result = HttpUtil.getAllSportsData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            Integer success = (Integer) resultObj.get("success");
            if (success != null) {
                if (1 == success) {
                    List<Map<String, Object>> matches = (List<Map<String, Object>>) resultObj.get("result");
                    if (matches != null && !matches.isEmpty()) {
                        for (Map<String, Object> ml : matches) {
                            String eventLive = (String) ml.get("event_live");
                            if (StringUtils.equals("1", eventLive)) {
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
        }
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
