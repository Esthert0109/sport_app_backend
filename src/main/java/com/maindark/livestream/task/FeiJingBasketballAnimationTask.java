package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballAnimationDao;
import com.maindark.livestream.dao.FeiJingFootballAnimationDao;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
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
public class FeiJingBasketballAnimationTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingBasketballAnimationDao feiJingBasketballAnimationDao;

    @Scheduled(cron = "0 */1 * * * *")
    public void getAnimation(){
        String url = feiJingConfig.getBasketballAnimation();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Object> matchList = (List<Object>) resultObj.get("list");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match->{
                String str = (String)match;
                String[] arr = str.split("!");
                String[] data = arr[0].split("\\^");
                Integer matchId = Integer.parseInt(data[0]);
                int existed = feiJingBasketballAnimationDao.queryExisted(matchId);
                if(existed <=0) {
                    feiJingBasketballAnimationDao.insertData(matchId);
                }
            });
        }
    }
}
