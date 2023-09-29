package com.maindark.livestream.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.maindark.livestream.dao.FootballCompetitionDao;
import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.domain.FootballCompetition;
import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FootBallService {
  ////https://open.sportnanoapi.com/api/v5/football/category/list
    @Resource
    NamiConfig namiConfig;

    @Resource
    FootballCompetitionDao footballCompetitionDao;

    @Resource
    FootballMatchDao footballMatchDao;

  private Integer maxCompetitionIdFromApi = 0;

  private Integer maxCompetitionTimeFromApi = 0;

    public List<Map<String,Integer>> getFootBallMatchList(String competitionDate,Integer id,Integer time){
     /* String url = namiConfig.getHost() + namiConfig.getFootballUrl() + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&date="+competitionDate;
      String result = HttpUtil.getNaMiData(url);
      Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
      Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
      List<Map<String,Object>> list = (List<Map<String,Object>>)results.get("match");
      Integer maxId = footballMatchDao.getMaxId();
      Integer maxUpdatedAt = footballMatchDao.getMaxUpdatedAt();
      System.out.println(maxUpdatedAt);
      System.out.println(maxId);*/
      List<Map<String,Integer>> list = new ArrayList<>();
      Map<String,Integer> map = getMap(id,time);
      list.add(map);
      return list;
    }

    public Map<String,Integer> getMap(Integer maxId,Integer maxTime){
      maxCompetitionIdFromApi += maxId;
      maxCompetitionTimeFromApi += maxTime;
      Map<String,Integer> map = new HashMap<>();
      map.put("Id",maxCompetitionIdFromApi);
      map.put("Time",maxCompetitionTimeFromApi);
      return map;
    }







}
