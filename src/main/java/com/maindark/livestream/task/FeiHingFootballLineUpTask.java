package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.FeiJingFootballLineUpDao;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballLineUp;
import com.maindark.livestream.enums.IsFirst;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.enums.TeamEnum;
import com.maindark.livestream.feiJing.FeiJingConfig;
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
public class FeiHingFootballLineUpTask {

    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingFootballLineUpDao feiJingFootballLineUpDao;

    @Resource
    FeiJingFootballMatchDao feiJingFootballMatchDao;

    @Scheduled(cron = "0 */2 * * * ? ")
    public void getFeiJingFootballMatchLineUp(){
        String url = feiJingConfig.getLineup();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> lineupList = (List<Map<String,Object>>) resultObj.get("lineupList");
            if(lineupList != null && !lineupList.isEmpty()){
                lineupList.forEach(match ->{
                    Integer matchId = (Integer)match.get("matchId");
                    String homeFormation = (String)match.get("homeArray");
                    String awayFormation = (String)match.get("awayArray");
                    feiJingFootballMatchDao.updateFormationByMatchId(homeFormation,awayFormation,matchId);
                    JSONArray homeLineup = (JSONArray) match.get("homeLineup");
                    JSONArray awayLineup = (JSONArray) match.get("awayLineup");
                    JSONArray homeBackup = (JSONArray) match.get("homeBackup");
                    JSONArray awayBackup = (JSONArray) match.get("awayBackup");
                    getMatchLineUp(homeLineup,matchId,homeBackup, TeamEnum.HOME.getCode());
                    getMatchLineUp(awayLineup,matchId,awayBackup, TeamEnum.AWAY.getCode());
                });
            }
        }


    }

    private void getMatchLineUp(JSONArray startingLineups, Integer matchId,JSONArray substitutes,String teamType){
        setAllSportsFootballMatchLineUp(startingLineups, matchId, IsFirst.YES.getCode(),teamType);
        setAllSportsFootballMatchLineUp(substitutes, matchId,IsFirst.NO.getCode(), teamType);
    }

    private void setAllSportsFootballMatchLineUp(JSONArray jsonArray, Integer matchId, Integer first, String teamType) {
        if(jsonArray != null){
            // right code
            /*for (Object o : jsonArray) {
                Map<String, Object> map = (Map<String, Object>) o;
                String playerName = (String) map.get("nameChs");
                String playerNumber = (String) map.get("number");
                String playerPosition = (String) map.get("positionId");
                Integer playerId = (Integer) map.get("playerId");
                FeiJingFootballLineUp footballLineUp = getAllSportsLineUp(playerId, Integer.parseInt(playerNumber), playerPosition, matchId, playerName, first, teamType);
                int exist = feiJingFootballLineUpDao.queryExists(playerId, matchId);
                if (exist <= 0) {
                    feiJingFootballLineUpDao.insert(footballLineUp);
                }
            }*/
            // TODO
            for(int i=0;i<jsonArray.size();i++){
                Map<String, Object> map = (Map<String, Object>) jsonArray.get(i);
                String playerName = (String) map.get("nameChs");
                String playerNumber = (String) map.get("number");
                String playerPosition = String.valueOf(i);
                Integer playerId = (Integer) map.get("playerId");
                FeiJingFootballLineUp footballLineUp = getAllSportsLineUp(playerId, Integer.parseInt(playerNumber), playerPosition, matchId, playerName, first, teamType);
                int exist = feiJingFootballLineUpDao.queryExists(playerId, matchId);
                if (exist <= 0) {
                    feiJingFootballLineUpDao.insert(footballLineUp);
                }
            }
        }
    }

    private FeiJingFootballLineUp getAllSportsLineUp(Integer playerId, Integer playNumber, String playPosition, Integer matchId, String playerName, Integer first, String teamType) {
        FeiJingFootballLineUp footballLineUp = new FeiJingFootballLineUp();
        if(StringUtils.equals("0",teamType)){
            footballLineUp.setType(LineUpType.HOME.getType());
        } else {
            footballLineUp.setType(LineUpType.AWAY.getType());
        }
        footballLineUp.setPlayerId(playerId);
        footballLineUp.setMatchId(matchId);
        footballLineUp.setShirtNumber(playNumber);
        footballLineUp.setPosition(Integer.parseInt(playPosition));
        footballLineUp.setPlayerName(playerName);
        footballLineUp.setFirst(first);
        return footballLineUp;
    }



}
