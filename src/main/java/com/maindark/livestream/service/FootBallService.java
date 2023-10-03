package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.maindark.livestream.dao.FootballCompetitionDao;
import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.FootballTeamDao;
import com.maindark.livestream.domain.FootballCompetition;
import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.vo.FootballMatchVo;
import com.maindark.livestream.vo.FootballTeamVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Resource
    FootballTeamDao footballTeamDao;

  private Integer maxCompetitionIdFromApi = 0;

  private Integer maxCompetitionTimeFromApi = 0;

    public List<FootballMatchVo> getFootBallMatchList(String competitionName, String teamName){
      List<FootballMatchVo> list = null;
      if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
        throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
      }
      LocalDate now = LocalDate.now();
      LocalDate tomorrow = now.plusDays(1);
      DateTimeFormatter dateFormatter
              = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String nowDate = now.toString();
      String tomorrowDate = tomorrow.toString();
      long nowSeconds = LocalDate.parse(nowDate, dateFormatter)
              .atStartOfDay(ZoneOffset.UTC)
              .toInstant()
              .toEpochMilli() / 1000;
      long tomorrowSeconds = LocalDate.parse(nowDate, dateFormatter)
              .atStartOfDay(ZoneOffset.UTC)
              .toInstant()
              .toEpochMilli() /1000;
      if(!StringUtils.isBlank(competitionName)){
          list = footballMatchDao.getFootballMatchByCompetitionName(competitionName,nowSeconds,tomorrowSeconds);
      } else if(!StringUtils.isBlank(teamName)){
          list = footballMatchDao.getFootballMatchByTeamName(teamName,nowSeconds,tomorrowSeconds);
      }
      if(list != null && !list.isEmpty()){
        int size = list.size();
        for(int i=0;i<size;i++) {
          FootballMatchVo footballMatchVo = list.get(i);
          Integer homeTeamId = footballMatchVo.getHomeTeamId();
          Integer awayTeamId = footballMatchVo.getAwayTeamId();
          FootballTeamVo homeTeam = footballTeamDao.getTeamLogoAndNameById(homeTeamId);
          FootballTeamVo awayTeam = footballTeamDao.getTeamLogoAndNameById(awayTeamId);
          footballMatchVo.setHomeTeamLogo(homeTeam.getLogo());
          footballMatchVo.setAwayTeamLogo(awayTeam.getLogo());
          Long matchTime = footballMatchVo.getMatchTime() * 1000;
          String timeStr = getTimeStr(matchTime);
          footballMatchVo.setMatchTimeStr(timeStr);
        }
      }
      return list;
    }

    public Map<String,Integer> getMap(Integer maxId,Integer maxTime){
      /* String url = namiConfig.getHost() + namiConfig.getFootballUrl() + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&date="+competitionDate;
      String result = HttpUtil.getNaMiData(url);
      Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
      Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
      List<Map<String,Object>> list = (List<Map<String,Object>>)results.get("match");
      Integer maxId = footballMatchDao.getMaxId();
      Integer maxUpdatedAt = footballMatchDao.getMaxUpdatedAt();
      System.out.println(maxUpdatedAt);
      System.out.println(maxId);*/
      maxCompetitionIdFromApi += maxId;
      maxCompetitionTimeFromApi += maxTime;
      Map<String,Integer> map = new HashMap<>();
      map.put("Id",maxCompetitionIdFromApi);
      map.put("Time",maxCompetitionTimeFromApi);

      return map;
    }

    public String getTimeStr(Long time) {
      Date sol = new Date(time);
      DateFormat obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
      String timeStr = obj.format(sol);
      timeStr = timeStr.substring(12,16);
      return timeStr;

    }






}
