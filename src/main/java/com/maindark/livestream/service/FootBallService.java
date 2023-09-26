package com.maindark.livestream.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FootBallService {
  ////https://open.sportnanoapi.com/api/v5/football/category/list
    @Resource
    NamiConfig namiConfig;

    public String getFootBallMatchList(String competitionDate){
      String url = namiConfig.getHost() + namiConfig.getFootballUrl() + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&date="+competitionDate;
      String result = HttpUtil.getNaMiData(url);
      JSONObject resultObj = JSON.parseObject(result);
      Integer code = (Integer)resultObj.get("code");
      if(0 == code) {
        Gson gson = new Gson();
        Map<String,Object> map = new HashMap<String,Object>();
        map = (Map<String,Object>) gson.fromJson(result, map.getClass());
        Map<String,Object> results = (Map<String, Object>) map.get("results");
        if(results != null) {
          List<Map<String,Object>> list = (List<Map<String, Object>>) results.get("competition");
          System.out.println(list);
          for(int i=0;i<list.size();i++) {
           Map<String,Object> competition = list.get(i);
          }
        }
        return result;
      } else {
        log.error("nami football error result :{}",result);
        throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
      }


    }






}
