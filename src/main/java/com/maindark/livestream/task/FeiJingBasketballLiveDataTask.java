package com.maindark.livestream.task;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.dao.FeiJingBasketballMatchLiveDataDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballMatchLiveData;
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
public class FeiJingBasketballLiveDataTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingBasketballMatchLiveDataDao feiJingBasketballMatchLiveDataDao;

    @Scheduled(cron = "0 */2 * * * ? ")
    public void getBasketballLiveData(){
        String url = feiJingConfig.getBasketballLineup();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
            if(matchList != null && !matchList.isEmpty()) {
                matchList.forEach(match ->{
                    Integer matchId = (Integer) match.get("matchId");
                    FeiJingBasketballMatchLiveData feiJingBasketballMatchLiveData = new FeiJingBasketballMatchLiveData();
                    feiJingBasketballMatchLiveData.setMatchId(matchId);
                    JSONArray homePlayerList = (JSONArray) match.get("homePlayerList");
                    if(homePlayerList != null) {
                        String[] homeData = getLiveResultData(homePlayerList);
                        feiJingBasketballMatchLiveData.setHBlocks(homeData[0]);
                        feiJingBasketballMatchLiveData.setHFieldGoals(homeData[1]);
                        feiJingBasketballMatchLiveData.setHFreeThrows(homeData[2]);
                        feiJingBasketballMatchLiveData.setHPersonalFouls(homeData[3]);
                        feiJingBasketballMatchLiveData.setHRebounds(homeData[4]);
                        feiJingBasketballMatchLiveData.setHSteals(homeData[5]);
                        feiJingBasketballMatchLiveData.setHThreePointGoals(homeData[6]);
                        feiJingBasketballMatchLiveData.setHTurnOvers(homeData[7]);
                    }
                    JSONArray awayPlayerList = (JSONArray) match.get("awayPlayerList");
                    if(awayPlayerList != null) {
                        String[] awayData = getLiveResultData(awayPlayerList);
                        feiJingBasketballMatchLiveData.setABlocks(awayData[0]);
                        feiJingBasketballMatchLiveData.setAFieldGoals(awayData[1]);
                        feiJingBasketballMatchLiveData.setAFreeThrows(awayData[2]);
                        feiJingBasketballMatchLiveData.setAPersonalFouls(awayData[3]);
                        feiJingBasketballMatchLiveData.setARebounds(awayData[4]);
                        feiJingBasketballMatchLiveData.setASteals(awayData[5]);
                        feiJingBasketballMatchLiveData.setAThreePointGoals(awayData[6]);
                        feiJingBasketballMatchLiveData.setATurnOvers(awayData[7]);
                    }
                    int existed = feiJingBasketballMatchLiveDataDao.queryExist(matchId);
                    if(existed <=0){
                        feiJingBasketballMatchLiveDataDao.insertData(feiJingBasketballMatchLiveData);
                    } else {
                        feiJingBasketballMatchLiveDataDao.updateData(feiJingBasketballMatchLiveData);
                    }
                });
            }
        }
    }

    private String[] getLiveResultData(JSONArray playerList) {
        String[] arr = new String[8];
        int Blocks = 0;
        int FieldGoals = 0;
        int FreeThrows = 0;
        int PersonalFouls = 0;
        int Rebounds = 0;
        int Steals = 0;
        int ThreePointGoals =0;
        int TurnOvers = 0;
        for (Object o : playerList) {
            Map<String, Object> ml = (Map<String, Object>) o;
            String blocks = (String) ml.get("block");
            String fieldGoalsAttempts = (String) ml.get("shoot");
            String freeThrowsGoalsAttempts = (String) ml.get("freeThrowShoot");
            String personalFouls = (String) ml.get("foul");
            String offensiveRebound = (String) ml.get("offensiveRebound");
            String defensiveRebound = (String) ml.get("defensiveRebound");
            String totalRebounds = String.valueOf(Integer.parseInt(offensiveRebound) + Integer.parseInt(defensiveRebound));
            String steals = (String) ml.get("steal");
            String threePointGoalsMade = (String) ml.get("threePointHit");
            String turnovers = (String) ml.get("turnover");
            Blocks += Integer.parseInt(StringUtils.equals(blocks,"")?"0":blocks);
            FieldGoals += Integer.parseInt(StringUtils.equals(fieldGoalsAttempts,"")?"0":fieldGoalsAttempts);
            FreeThrows += Integer.parseInt(StringUtils.equals(freeThrowsGoalsAttempts,"")?"0":freeThrowsGoalsAttempts);
            PersonalFouls += Integer.parseInt(StringUtils.equals(personalFouls,"")?"0":personalFouls);
            Rebounds += Integer.parseInt(StringUtils.equals(totalRebounds,"")?"0":totalRebounds);
            Steals += Integer.parseInt(StringUtils.equals(steals,"")?"0":steals);
            ThreePointGoals += Integer.parseInt(StringUtils.equals(threePointGoalsMade,"")?"0":threePointGoalsMade);
            TurnOvers += Integer.parseInt(StringUtils.equals(turnovers,"")?"0":turnovers);

        }
        arr[0] = String.valueOf(Blocks);
        arr[1] = String.valueOf(FieldGoals);
        arr[2] = String.valueOf(FreeThrows);
        arr[3] = String.valueOf(PersonalFouls);
        arr[4] = String.valueOf(Rebounds);
        arr[5] = String.valueOf(Steals);
        arr[6] = String.valueOf(ThreePointGoals);
        arr[7] = String.valueOf(TurnOvers);
        return arr;
    }
}
