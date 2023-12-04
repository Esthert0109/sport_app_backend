package com.maindark.livestream.service;

import com.maindark.livestream.config.PushAndPlayConfig;
import com.maindark.livestream.util.DateUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PushAndPlayService {

    @Resource
    PushAndPlayConfig pushAndPlayConfig;

    public Map<String,Object> getPushUrl(){
        String time = DateUtil.convertLocalDateToTime();
        log.info("push url time:{}",time);
        Map<String,Object> map = new HashMap<>();
        String host = pushAndPlayConfig.getLiveHost();
        String pushCode = pushAndPlayConfig.getPushCode();
        map.put("time",time);
        map.put("host",host);
        map.put("code",pushCode);
        return map;
    }
}
