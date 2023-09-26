package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class FootBallTask {

    @Resource
    NamiConfig namiConfig;

    /* execute every hour every day*/
    @Scheduled(cron = "0 */10 * * * ?")
    //second, minute, hour, day of month, month, day(s) of week
    public void getFootBallCompetition(){
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = dateObj.format(formatter);
        String url = namiConfig.getHost() + namiConfig.getFootballUrl() + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&date="+date;
        String result = HttpUtil.getNaMiData(url);
        JSONObject resultObj = JSON.parseObject(result);
        Integer code = (Integer)resultObj.get("code");
        if(0 == code) {
            List<Object> competitionList = (List)resultObj.get("competition");
            System.out.println(competitionList);
        } else {
            log.error("nami football error result :{}",result);
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }


    }
}
