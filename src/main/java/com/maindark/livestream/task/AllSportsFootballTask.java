package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsAwayMatchLineUpDao;
import com.maindark.livestream.dao.AllSportsHomeMatchLineUpDao;
import com.maindark.livestream.domain.AllSportsAwayMatchLineUp;
import com.maindark.livestream.domain.AllSportsHomeMatchLineUp;
import com.maindark.livestream.domain.HomeMatchLineUp;
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
    AllSportsAwayMatchLineUpDao allSportsAwayMatchLineUpDao;
   @Resource
   AllSportsHomeMatchLineUpDao allSportsHomeMatchLineUpDao;


    @Scheduled(cron = "0 */10 * * * ? ")
    public void getAllSportsFootballMatchLineUp(){
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getLivescore());
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
                        //getHomeMatchLineUp(startingLineups);
                    }
                }
            }
        }
    }



    /*private List<HomeMatchLineUp> getHomeMatchLineUp(JSONArray jsonArray){
        if(jsonArray != null){
            int size = jsonArray.size();
            for(int i=0;i<size;i++){
                Map<String,Object> map = (Map<String,Object>)jsonArray.get(i);
                String playerName = (String)map.get("player");
                Integer playerNumber = (Integer)map.get("player_number");
                Integer playerPosition = (Integer)map.get("player_position");
                Number playerKey = (Number)map.get("player_key");
                AllSportsHomeMatchLineUp  allSportsHomeMatchLineUp = getAllSportsHomeLineUp(playerKey.toString(),playerNumber,playerPosition);
                AllSportsAwayMatchLineUp allSportsHomeMatchLineUpFromDb = homeMatchLineUpDao.getAwayMatchLineUpById(playerKey.longValue());
                if(allSportsHomeMatchLineUpFromDb == null){

                } else {

                }


            }
        }
        return null;
    }
*/
    private AllSportsHomeMatchLineUp getAllSportsHomeLineUp(String playerId,Integer playNumber,Integer playPosition) {
        AllSportsHomeMatchLineUp allSportsHomeMatchLineUp = new AllSportsHomeMatchLineUp();
        allSportsHomeMatchLineUp.setId(Long.parseLong(playerId));
        allSportsHomeMatchLineUp.setShirtNumber(playNumber);
        allSportsHomeMatchLineUp.setPosition(playPosition);
        String url = allSportsConfig.getAllSportsApi(allSportsConfig.getPlayers()).replace("{}",playerId);
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if (resultObj != null && !resultObj.isEmpty()) {
            int success = (Integer) resultObj.get("success");
            if(1 == success){
                Map<String,Object> playMap = (Map<String,Object>)resultObj.get("result");
                String captain = (String)playMap.get("player_is_captain");
                if(StringUtils.isBlank(captain)){
                    allSportsHomeMatchLineUp.setCaptain(0);
                } else {
                    allSportsHomeMatchLineUp.setCaptain(1);
                }
                String playerName = (String)playMap.get("player_name");
                String playerImage = (String)playMap.get("player_image");
                String playerRating = (String)playMap.get("player_rating");
                allSportsHomeMatchLineUp.setFirst(1);
                allSportsHomeMatchLineUp.setPlayerName(playerName);
                allSportsHomeMatchLineUp.setPlayerLogo(playerImage);
                allSportsHomeMatchLineUp.setRating(playerRating);

            }
        }
        return null;
    }


}
