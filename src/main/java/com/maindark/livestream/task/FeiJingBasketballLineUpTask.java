package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.dao.FeiJingBasketballLineUpDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballLineUp;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@EnableScheduling
public class FeiJingBasketballLineUpTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingBasketballLineUpDao feiJingBasketballLineUpDao;

    @Scheduled(cron = "0 */2 * * * ? ")
    public void getBasketballLineUp(){
        String url = feiJingConfig.getBasketballLineup();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
            if(matchList != null && !matchList.isEmpty()) {
                matchList.forEach(match ->{
                    Integer matchId = (Integer) match.get("matchId");
                    JSONArray homePlayerList = (JSONArray) match.get("homePlayerList");
                    if(homePlayerList != null) {
                        getMatchLineUp(homePlayerList,matchId, LineUpType.HOME.getType());
                    }

                    JSONArray awayPlayerList = (JSONArray) match.get("awayPlayerList");
                    if(awayPlayerList != null) {
                        getMatchLineUp(awayPlayerList,matchId, LineUpType.AWAY.getType());
                    }

                });
            }
        }
    }

    private void getMatchLineUp(JSONArray lineupArray, Integer matchId, Integer teamType) {
        if (lineupArray != null && !lineupArray.isEmpty()) {
            for (Object o : lineupArray) {
                Map<String, Object> ml = (Map<String, Object>) o;
                FeiJingBasketballLineUp feiJingBasketballLineUp = getAllSportsLineUp(ml, matchId, teamType);
                int exist = feiJingBasketballLineUpDao.queryExist(feiJingBasketballLineUp.getPlayerId(), matchId);
                if (exist <= 0) {
                    feiJingBasketballLineUpDao.insertData(feiJingBasketballLineUp);
                } else {
                    feiJingBasketballLineUpDao.updateData(feiJingBasketballLineUp);
                }
            }
        }
    }

    private FeiJingBasketballLineUp getAllSportsLineUp(Map<String, Object> ml, Integer matchId, Integer teamType) {
        FeiJingBasketballLineUp feiJingBasketballLineUp = new FeiJingBasketballLineUp();
        Integer playerId = (Integer) ml.get("playerId");
        String playerName = (String) ml.get("playerChs");
        /*出场时间*/
        String minutes = (String) ml.get("playtime");
        /*得分*/
        String point = (String) ml.get("score");
        /*助攻*/
        String assists = (String) ml.get("assist");
        /*抢断*/
        String steals = (String) ml.get("steal");
        /*篮板 */
        String offensiveRebound = (String) ml.get("offensiveRebound");
        String defensiveRebound = (String) ml.get("defensiveRebound");
        String totalRebounds = String.valueOf(Integer.parseInt(offensiveRebound) + Integer.parseInt(defensiveRebound));
        /*罚球 总数*/
        String freeThrowsGoalsAttempts = (String) ml.get("freeThrowShoot");
        /*罚球命中*/
        String freeThrowsGoalsMade = (String) ml.get("freeThrowHit");
        /*犯规*/
        String personalFouls = (String) ml.get("foul");
        /*失误*/
        String turnovers = (String) ml.get("turnover");
        /*三分 总数*/
        String threePointGoalsAttempts = (String) ml.get("threePointShoot");
        /*三分命中*/
        String threePointGoalsMade = (String) ml.get("threePointHit");
        /*盖帽*/
        String blocks = (String) ml.get("block");
        /*投篮总数*/
        String fieldGoalsAttempts = (String) ml.get("shoot");
        /*投篮命中*/
        String fieldGoalsMade = (String) ml.get("shootHit");
        feiJingBasketballLineUp.setPlayerId(playerId);
        feiJingBasketballLineUp.setMatchId(matchId);
        feiJingBasketballLineUp.setType(teamType);
        feiJingBasketballLineUp.setPlayerName(playerName);
        feiJingBasketballLineUp.setBlocks(blocks);
        feiJingBasketballLineUp.setMinutes(minutes);
        feiJingBasketballLineUp.setAssists(assists);
        feiJingBasketballLineUp.setTurnovers(turnovers);
        feiJingBasketballLineUp.setPoint(point);
        feiJingBasketballLineUp.setSteals(steals);
        feiJingBasketballLineUp.setFieldGoalsAttempts(fieldGoalsAttempts);
        feiJingBasketballLineUp.setFieldGoalsMade(fieldGoalsMade);
        feiJingBasketballLineUp.setPersonalFouls(personalFouls);
        feiJingBasketballLineUp.setThreePointGoalsAttempts(threePointGoalsAttempts);
        feiJingBasketballLineUp.setThreePointGoalsMade(threePointGoalsMade);
        feiJingBasketballLineUp.setFreeThrowsGoalsAttempts( freeThrowsGoalsAttempts);
        feiJingBasketballLineUp.setFreeThrowsGoalsMade(freeThrowsGoalsMade);
        feiJingBasketballLineUp.setTotalRebounds(totalRebounds);
        return feiJingBasketballLineUp;
    }




}
