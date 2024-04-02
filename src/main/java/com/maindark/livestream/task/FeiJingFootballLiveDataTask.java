package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.FeiJingFootballMatchLiveDataDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatchLiveData;
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
public class FeiJingFootballLiveDataTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingFootballMatchLiveDataDao feiJingFootballMatchLiveDataDao;


    @Scheduled(cron = "0 */2 * * * ? ")
    public void getLiveData(){
        String url = feiJingConfig.getLiveData();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(resultObj != null && !resultObj.isEmpty()){
            List<Map<String,Object>> technic = (List<Map<String,Object>>) resultObj.get("technic");
            if(technic != null && !technic.isEmpty()){
                technic.forEach(match ->{
                    Integer matchId = (Integer)match.get("matchId");
                    String technicCount = (String)match.get("technicCount");
                    if(!StringUtils.equals("",technicCount)) {
                        String[] technicArr = technicCount.split(";");
                        FeiJingFootballMatchLiveData feiJingFootballMatchLiveData = dealTechnicCount(technicArr);
                        feiJingFootballMatchLiveData.setMatchId(matchId);
                        int existed = feiJingFootballMatchLiveDataDao.queryExisted(matchId);
                        if(existed <=0) {
                            feiJingFootballMatchLiveDataDao.insertData(feiJingFootballMatchLiveData);
                        } else {
                            feiJingFootballMatchLiveDataDao.updateData(feiJingFootballMatchLiveData);
                        }
                    }
                });
            }
        }
    }

    public FeiJingFootballMatchLiveData dealTechnicCount(String[] technicArr){
        FeiJingFootballMatchLiveData feiJingFootballMatchLiveData = new FeiJingFootballMatchLiveData();
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
        String homeRedCardNum = "0";
        String awayRedCardNum = "0";
        String homeYellowCardNum = "0";
        String awayYellowCardNum = "0";
        String homePenaltyNum = "0";
        String awayPenaltyNum = "0";
        for (String str:technicArr) {
           String[] detail = str.split(",");
           switch (detail[0]) {
               case "44":
                   homeAttackDangerNum = detail[1];
                   awayAttackDangerNum = detail[2];
                   break;
               case "43":
                   homeAttackNum = detail[1];
                   awayAttackNum = detail[2];
                   break;
               case "14":
                   homePossessionRate = detail[1];
                   awayPossessionRate = detail[2];
                   break;
               case "3":
                   homeBiasNum = detail[1];
                   awayBiasNum = detail[2];
                   break;
               case "4":
                   homeShootGoalNum = detail[1];
                   awayShootGoalNum = detail[2];
                   break;
               case "5":
                   homePenaltyNum = detail[1];
                   awayPenaltyNum = detail[2];
                   break;
               case "6":
                   homeCornerKickNum = detail[1];
                   awayCornerKickNum = detail[2];
                   break;
               case "11":
                   homeYellowCardNum = detail[1];
                   awayYellowCardNum = detail[2];
                   break;
               case"13":
                   homeRedCardNum = detail[1];
                   awayRedCardNum = detail[2];
                   break;

           }
        }
        feiJingFootballMatchLiveData.setHomeAttackNum(StringUtils.equals("",homeAttackNum)?0:Integer.parseInt(homeAttackNum));
        feiJingFootballMatchLiveData.setAwayAttackNum(StringUtils.equals("",awayAttackNum)?0:Integer.parseInt(awayAttackNum));
        feiJingFootballMatchLiveData.setHomeAttackDangerNum(StringUtils.equals("",homeAttackDangerNum)?0:Integer.parseInt(homeAttackDangerNum));
        feiJingFootballMatchLiveData.setAwayAttackDangerNum(StringUtils.equals("",awayAttackDangerNum)?0:Integer.parseInt(awayAttackDangerNum));
        feiJingFootballMatchLiveData.setHomeBiasNum(StringUtils.equals("",homeBiasNum)?0:Integer.parseInt(homeBiasNum));
        feiJingFootballMatchLiveData.setAwayBiasNum(StringUtils.equals("",awayBiasNum)?0:Integer.parseInt(awayBiasNum));
        feiJingFootballMatchLiveData.setHomeCornerKickNum(StringUtils.equals("",homeCornerKickNum)?0:Integer.parseInt(homeCornerKickNum));
        feiJingFootballMatchLiveData.setAwayCornerKickNum(StringUtils.equals("",awayCornerKickNum)?0:Integer.parseInt(awayCornerKickNum));
        feiJingFootballMatchLiveData.setHomePossessionRate(homePossessionRate);
        feiJingFootballMatchLiveData.setAwayPossessionRate(awayPossessionRate);
        feiJingFootballMatchLiveData.setHomeShootGoalNum(StringUtils.equals("",awayShootGoalNum)?0:Integer.parseInt(homeShootGoalNum));
        feiJingFootballMatchLiveData.setAwayShootGoalNum(StringUtils.equals("",awayShootGoalNum)?0:Integer.parseInt(awayShootGoalNum));
        feiJingFootballMatchLiveData.setHomeYellowCardNum(StringUtils.equals("",homeYellowCardNum)?0:Integer.parseInt(homeYellowCardNum));
        feiJingFootballMatchLiveData.setAwayYellowCardNum(StringUtils.equals("",awayYellowCardNum)?0:Integer.parseInt(awayYellowCardNum));
        feiJingFootballMatchLiveData.setHomeRedCardNum(StringUtils.equals("",homeRedCardNum)?0:Integer.parseInt(homeRedCardNum));
        feiJingFootballMatchLiveData.setAwayRedCardNum(StringUtils.equals("",awayRedCardNum)?0:Integer.parseInt(awayRedCardNum));
        feiJingFootballMatchLiveData.setHomePenaltyNum(StringUtils.equals("",homePenaltyNum)?0:Integer.parseInt(homePenaltyNum));
        feiJingFootballMatchLiveData.setAwayPenaltyNum(StringUtils.equals("",awayPenaltyNum)?0:Integer.parseInt(awayPenaltyNum));
        return feiJingFootballMatchLiveData;
    }




}
