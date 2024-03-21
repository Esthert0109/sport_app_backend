package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingFootballAnimationDao;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.dao.FeiJingFootballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
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
public class FeiJingFootballAnimationTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingFootballAnimationDao feiJingFootballAnimationDao;

    @Scheduled(cron = "0 */1 * * * *")
    public void getAnimation(){
        String url = feiJingConfig.getFootballAnimation();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match->{
                Integer matchId = (Integer) match.get("matchId");
                int existed = feiJingFootballAnimationDao.queryExisted(matchId);
                if(existed <=0) {
                    feiJingFootballAnimationDao.insertData(matchId);
                }
            });
        }
    }
}
