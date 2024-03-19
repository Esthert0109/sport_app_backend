package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import com.maindark.livestream.feiJing.FeiJingBasketballConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FeiJingBasketballLatestMatchesTask {

    @Resource
    FeiJingBasketballMatchDao feijingBasketballMatchDao;

    @Resource
    FeiJingBasketballConfig feiJingBasketballConfig;


    @Scheduled(cron = "0 0 * * * *")
    public void updateMatches() {


        try {
            String url = feiJingBasketballConfig.getMatch();
            String result = HttpUtil.sendGet(url);
            Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
            List<Map<String, Object>> matchList = (List<Map<String, Object>>) resultObj.get("matchList");

            if (matchList != null && !matchList.isEmpty()) {
                for (Map<String, Object> match : matchList) {

                    FeijingBasketballMatch feijingBasketballMatch = parseMatch(match);
                    feijingBasketballMatchDao.insertData(feijingBasketballMatch);
                }
            }

            log.info("Basketball match update completed.");
        } catch (Exception e) {
            log.error("Failed to update basketball matches: {}", e.getMessage());
        }
    }


    private FeijingBasketballMatch parseMatch(Map<String, Object> match) {

        return null;
    }
}
