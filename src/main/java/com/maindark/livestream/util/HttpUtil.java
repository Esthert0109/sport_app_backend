package com.maindark.livestream.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HttpUtil {

    public static String getNaMiData(String url){
        RestTemplate restTemplate = new RestTemplate();
        log.info("send nami url:{}",url);
        String result = restTemplate.getForObject(url,String.class);
        log.info("nami result :{}",result);
        JSONObject resultObj = JSON.parseObject(result);
        return result;
    }
}
