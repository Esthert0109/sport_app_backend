package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.maindark.livestream.dao.BasketballMatchLiveDataDao;
import com.maindark.livestream.domain.BasketballMatchLiveData;
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
public class BasketballLiveDataTask {

    @Resource
    NamiConfig namiConfig;
    @Resource
    BasketballMatchLiveDataDao basketballMatchLiveDataDao;
    @Scheduled(cron = "0 */30 * * * ? ")
    public void getAllLiveData() {
        String url = namiConfig.getNormalUrl(namiConfig.getBasketballLiveUrl());
        String result = HttpUtil.getNaMiData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        Integer code = (Integer) resultObj.get("code");
        if (code == null) {
            log.error("get nami basketball live data error:{}", resultObj.get("err"));
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }
        if (code == 0) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) resultObj.get("results");
            if (results != null && !results.isEmpty()) {
                results.stream().forEach(ml -> {
                    BasketballMatchLiveData matchLiveData = insertOrUpdateMatchLiveData(ml);
                    int exist = basketballMatchLiveDataDao.queryExist(matchLiveData.getMatchId());
                    if(exist <=0){
                        basketballMatchLiveDataDao.insertData(matchLiveData);
                    } else {
                        basketballMatchLiveDataDao.updateData(matchLiveData);
                    }
                });
            }
        }

    }

    public BasketballMatchLiveData insertOrUpdateMatchLiveData(Map<String, Object> ml) {
        Number matchId = (Number) ml.get("id");
        Integer status = 0;
        Integer hFQuarter = 0;
        Integer hSQuarter = 0;
        Integer hTQuarter = 0;
        Integer h4Quarter = 0;
        Integer aFQuarter = 0;
        Integer aSQuarter = 0;
        Integer aTQuarter = 0;
        Integer a4Quarter = 0;
        Integer additionalHQuarter = 0;
        Integer additionalAQuarter = 0;

        List<Object> score = (List<Object>) ml.get("score");
        if(score != null && !score.isEmpty()) {
            status = (Integer)score.get(1);
            List<Integer> homeQuarters = (List<Integer>)score.get(3);
            if(homeQuarters != null && !homeQuarters.isEmpty()) {
                hFQuarter = homeQuarters.get(0);
                hSQuarter = homeQuarters.get(1);
                hTQuarter = homeQuarters.get(2);
                h4Quarter = homeQuarters.get(3);
                additionalHQuarter = homeQuarters.get(4);
            }
            List<Integer> awayQuarters = (List<Integer>)score.get(4);
            if(awayQuarters != null && !awayQuarters.isEmpty()) {
                aFQuarter = awayQuarters.get(0);
                aSQuarter = awayQuarters.get(1);
                aTQuarter = awayQuarters.get(2);
                a4Quarter = awayQuarters.get(3);
                additionalAQuarter = awayQuarters.get(4);
            }
        }
        Integer hFreeThrow = 0;
        Integer hNumPauseRemain = 0;
        Integer hNumOfFouls = 0;
        Integer hFreeThrowPercentage = 0;
        Integer hTotalPause = 0;
        Integer hThreeGoal = 0;
        Integer hTwoGoal = 0;
        Integer aFreeThrow = 0;
        Integer aNumPauseRemain = 0;
        Integer aNumOfFouls = 0;
        Integer aFreeThrowPercentage = 0;
        Integer aTotalPause = 0;
        Integer aThreeGoal = 0;
        Integer aTwoGoal = 0;
        Integer homeScore = hFQuarter + hSQuarter + hTQuarter + h4Quarter + additionalHQuarter;
        Integer awayScore = aFQuarter + aSQuarter + aTQuarter + a4Quarter + additionalAQuarter;
        JSONArray statistics= (JSONArray) ml.get("stats");
        if(statistics != null && !statistics.isEmpty()){
            for(int i=0;i<statistics.size();i++){
                List<Object> statistic = (List<Object>)statistics.get(i);
                Integer type = (Integer)statistic.get(0);
                switch (type){
                    case 1:
                        hThreeGoal= (Integer)statistic.get(1);
                        aThreeGoal = (Integer)statistic.get(2);
                        break;
                    case 2:
                        hTwoGoal = (Integer)statistic.get(1);
                        aTwoGoal = (Integer)statistic.get(2);
                        break;
                    case 3:
                        hFreeThrow = (Integer)statistic.get(1);
                        aFreeThrow = (Integer)statistic.get(2);
                        break;
                    case 4:
                        hNumPauseRemain = (Integer)statistic.get(1);
                        aNumPauseRemain = (Integer)statistic.get(2);
                        break;
                    case 5:
                        hNumOfFouls = (Integer)statistic.get(1);
                        aNumOfFouls = (Integer)statistic.get(2);
                        break;
                    case 6:
                        hFreeThrowPercentage = ((Number)statistic.get(1)).intValue();
                        aFreeThrowPercentage = ((Number)statistic.get(2)).intValue();
                        break;
                    case 7:
                        hTotalPause = (Integer) statistic.get(1);
                        aTotalPause = (Integer)statistic.get(2);
                        break;
                }
            }
        }
        BasketballMatchLiveData basketballMatchLiveData = new BasketballMatchLiveData();
        basketballMatchLiveData.setMatchId(matchId.longValue());
        basketballMatchLiveData.setStatus(status);
        basketballMatchLiveData.setHFQuarter(hFQuarter);
        basketballMatchLiveData.setHSQuarter(hSQuarter);
        basketballMatchLiveData.setHTQuarter(hTQuarter);
        basketballMatchLiveData.setH4Quarter(h4Quarter);
        basketballMatchLiveData.setAFQuarter(aFQuarter);
        basketballMatchLiveData.setASQuarter(aSQuarter);
        basketballMatchLiveData.setATQuarter(aTQuarter);
        basketballMatchLiveData.setA4Quarter(a4Quarter);
        basketballMatchLiveData.setHFreeThrow(hFreeThrow);
        basketballMatchLiveData.setAFreeThrow(aFreeThrow);
        basketballMatchLiveData.setANumOfFouls(aNumOfFouls);
        basketballMatchLiveData.setHNumOfFouls(hNumOfFouls);
        basketballMatchLiveData.setANumPauseRemain(aNumPauseRemain);
        basketballMatchLiveData.setHNumPauseRemain(hNumPauseRemain);
        basketballMatchLiveData.setAFreeThrowPercentage(aFreeThrowPercentage);
        basketballMatchLiveData.setHFreeThrowPercentage(hFreeThrowPercentage);
        basketballMatchLiveData.setATwoGoal(aTwoGoal);
        basketballMatchLiveData.setHTwoGoal(hTwoGoal);
        basketballMatchLiveData.setAThreeGoal(aThreeGoal);
        basketballMatchLiveData.setHThreeGoal(hThreeGoal);
        basketballMatchLiveData.setATotalPause(aTotalPause);
        basketballMatchLiveData.setHTotalPause(hTotalPause);
        basketballMatchLiveData.setHomeScore(homeScore);
        basketballMatchLiveData.setAwayScore(awayScore);
        return basketballMatchLiveData;
    }
}
