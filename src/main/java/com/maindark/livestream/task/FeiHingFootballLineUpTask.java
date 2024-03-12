package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.FeiJingFootballLineUpDao;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@EnableScheduling
public class FeiHingFootballLineUpTask {

    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingFootballLineUpDao feiJingFootballLineUpDao;

    @Scheduled(cron = "0 */2 * * * ? ")
    public void getFeiJingFootballMatchLineUp(){
        String url = feiJingConfig.getLineUp();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
    }


}
