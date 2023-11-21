package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.dao.BasketballLineUpDao;
import com.maindark.livestream.domain.BasketballLineUp;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
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
public class BasketballMatchLineUpTask {

    @Resource
    NamiConfig namiConfig;

    @Resource
    BasketballLineUpDao basketballLineUpDao;




//    @Scheduled(cron = "0 */2 * * * ? ")
    public void getMatchLineUp(){
        String url = namiConfig.getNormalUrl(namiConfig.getBasketballLiveUrl());
        String result = HttpUtil.getNaMiData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        Integer code = (Integer) resultObj.get("code");
        if (code == null) {
            log.error("get nami basketball line up error:{}", resultObj.get("err"));
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }
        if (code == 0) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) resultObj.get("results");
            if (results != null && !results.isEmpty()) {
                results.stream().forEach(ml -> {
                   insertOrUpdateMatchLiveData(ml);
                });
            }
        }
    }

    private void insertOrUpdateMatchLiveData(Map<String, Object> ml) {
        Number matchId = (Number) ml.get("id");
        JSONArray players = (JSONArray)ml.get("players");
        if(players != null && !players.isEmpty()) {
            // get home team line up
            JSONArray homePlayers = (JSONArray) players.get(0);
            if(homePlayers != null && !homePlayers.isEmpty()) {
                for(int i =0;i<homePlayers.size();i++) {
                    List<Object> homePlayer = (List<Object>) homePlayers.get(i);
                    BasketballLineUp basketballLineUp = getLineUp(homePlayer,matchId.longValue(),LineUpType.HOME.getType());
                    int exist = basketballLineUpDao.queryExist(basketballLineUp.getPlayerId(),basketballLineUp.getMatchId());
                    if(exist <=0){
                        basketballLineUpDao.insertData(basketballLineUp);
                    } else {
                        basketballLineUpDao.updateData(basketballLineUp);
                    }
                }
            }
            // get away team line up
            JSONArray awayPlayers = (JSONArray) players.get(1);
            if(awayPlayers != null && !awayPlayers.isEmpty() ) {
                for(int i=0;i<awayPlayers.size();i++){
                    List<Object> awayPlayer = (List<Object>) homePlayers.get(i);
                    BasketballLineUp basketballLineUp = getLineUp(awayPlayer,matchId.longValue(),LineUpType.AWAY.getType());
                    int exist = basketballLineUpDao.queryExist(basketballLineUp.getPlayerId(), basketballLineUp.getMatchId());
                    if(exist <=0){
                        basketballLineUpDao.insertData(basketballLineUp);
                    } else {
                        basketballLineUpDao.updateData(basketballLineUp);
                    }
                }
            }
        }
    }

    public BasketballLineUp getLineUp(List<Object> objects,Long matchId,Integer type){
        BasketballLineUp basketballLineUp = new BasketballLineUp();
        Number playerId = (Number)objects.get(0);
        String playerName = (String)objects.get(1);
        String data = (String)objects.get(6);
        String[] dataArr = data.split("[/^]+",19);
        if(dataArr != null && dataArr.length >0) {
            String minutes = dataArr[0];
            String fieldGoalsAttempts = dataArr[1].split("-",2)[0];
            String fieldGoalsMade = dataArr[1].split("-",2)[1];
            String threePointGoalsAttempts = dataArr[2].split("-",2)[0];
            String threePointGoalsMade = dataArr[2].split("-",2)[1];
            String freeThrowsGoalsAttempts = dataArr[3].split("-",2)[0];
            String freeThrowsGoalsMade = dataArr[3].split("-",2)[1];
            String totalRebounds = dataArr[6];
            String assists = dataArr[7];
            String steals = dataArr[8];
            String blocks = dataArr[9];
            String turnovers = dataArr[10];
            String personalFouls = dataArr[11];
            String point = dataArr[13];
            basketballLineUp.setAssists(assists);
            basketballLineUp.setMinutes(minutes);
            basketballLineUp.setBlocks(blocks);
            basketballLineUp.setPoint(point);
            basketballLineUp.setMatchId(matchId.longValue());
            basketballLineUp.setFieldGoalsAttempts(fieldGoalsAttempts);
            basketballLineUp.setFieldGoalsMade(fieldGoalsMade);
            basketballLineUp.setSteals(steals);
            basketballLineUp.setType(type);
            basketballLineUp.setPersonalFouls(personalFouls);
            basketballLineUp.setTurnovers(turnovers);
            basketballLineUp.setPlayerId(playerId.longValue());
            basketballLineUp.setPlayerName(playerName);
            basketballLineUp.setTotalRebounds(totalRebounds);
            basketballLineUp.setFreeThrowsGoalsAttempts(freeThrowsGoalsAttempts);
            basketballLineUp.setFreeThrowsGoalsMade(freeThrowsGoalsMade);
            basketballLineUp.setThreePointGoalsAttempts(threePointGoalsAttempts);
            basketballLineUp.setThreePointGoalsMade(threePointGoalsMade);
        }
        return basketballLineUp;
    }

}
