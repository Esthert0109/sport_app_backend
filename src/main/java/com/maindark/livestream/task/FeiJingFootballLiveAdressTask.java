package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.maindark.livestream.dao.FeiJingLiveAddressDao;
import com.maindark.livestream.domain.feijing.FeiJingLiveAddress;
import com.maindark.livestream.enums.SportTypeEnum;
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
public class FeiJingFootballLiveAdressTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingLiveAddressDao feiJingLiveAddressDao;

    @Scheduled(cron = "0 */1 * * * *")
    public void getFootballLiveAddress(){
        String url = feiJingConfig.getLiveFootballAddress();
        String accessKey = feiJingConfig.getLiveAccessKey();
        String secretKey = feiJingConfig.getLiveSecretKey();
        String res = HttpUtil.getLiveAddress(url,secretKey,accessKey);
        Map<String,Object> resultObj = JSON.parseObject(res,Map.class);
        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("data");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match ->{
               String matchId = (String)match.get("thirdId");
               if(!StringUtils.equals("",matchId)) {
                   Boolean hasLive = (Boolean)match.get("hasLive");
                   if(hasLive) {
                       JSONArray streams = (JSONArray)match.get("streams");
                       if(streams != null && !streams.isEmpty()) {
                           FeiJingLiveAddress feiJingLiveAddress = saveStreamData(matchId,streams);
                           int existed = feiJingLiveAddressDao.queryFootballLiveExisted(Integer.parseInt(matchId));
                           if(existed <=0) {
                               feiJingLiveAddressDao.insertData(feiJingLiveAddress);
                           }
                       }
                   }
               }
            });
        }
    }

    private FeiJingLiveAddress saveStreamData(String matchId, JSONArray streams) {
        FeiJingLiveAddress feiJingLiveAddress = new FeiJingLiveAddress();
        String[] addr = new String[3];
        for (int i=0;i< streams.size();i++){
            Map<String, Object> map = (Map<String, Object>) streams.get(i);
            String streamName = (String)map.get("streamName");
            addr[i] = streamName;
        }
        feiJingLiveAddress.setSportId(SportTypeEnum.FOOTBALL.getCode());
        feiJingLiveAddress.setMatchId(Integer.parseInt(matchId));
        feiJingLiveAddress.setPushUrl1(addr[0]);
        feiJingLiveAddress.setPushUrl2(addr[1]);
        return feiJingLiveAddress;
    }

}
