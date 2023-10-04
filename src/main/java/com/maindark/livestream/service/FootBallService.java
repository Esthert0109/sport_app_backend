package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.*;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.vo.FootballMatchVo;
import com.maindark.livestream.vo.FootballTeamVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FootBallService {
    @Resource
    NamiConfig namiConfig;

    @Resource
    FootballMatchDao footballMatchDao;

    @Resource
    FootballTeamDao footballTeamDao;

    @Resource
    FootballCompetitionDao footballCompetitionDao;
  @Resource
  HomeMatchLineUpDao homeMatchLineUpDao;

  @Resource
  AwayMatchLineUpDao awayMatchLineUpDao;


  private Integer maxCompetitionIdFromApi = 0;

  private Integer maxCompetitionTimeFromApi = 0;

  public String getNormalUrl(String normalUrl){
    String url = namiConfig.getHost() + normalUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey();
    return url;
  }

    public List<FootballMatchVo> getFootBallMatchList(String competitionName, String teamName){
      List<FootballMatchVo> list = null;
      if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
        throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
      }
      LocalDate now = LocalDate.now();
      LocalDate tomorrow = now.plusDays(1);
      long nowSeconds = DateUtil.convertDateToLongTime(now);
      long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
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
          String timeStr = DateUtil.interceptTime(matchTime);
          footballMatchVo.setMatchTimeStr(timeStr);
          Integer statusId = footballMatchVo.getStatusId();
          if(1 == statusId){
            footballMatchVo.setStatusStr("未");
          } else if(2==statusId){
            footballMatchVo.setStatusStr("上半场");
          } else if(3==statusId){
            footballMatchVo.setStatusStr("中场");
          } else if(4== statusId){
            footballMatchVo.setStatusStr("下半场");
          } else if(5== statusId){
            footballMatchVo.setStatusStr("加时赛");
          }
        }
      }
      return list;
    }

    public List<Map<String,Object>> getMatchList(String competitionDate){
      String url = getNormalUrl(namiConfig.getFootballUrl())+"&date="+ competitionDate;
      String result = HttpUtil.getNaMiData(url);
      Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
      Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
      List<Map<String,Object>> matchList = (List<Map<String,Object>>)results.get("match");
      List<Map<String,Object>> competitionList = (List<Map<String,Object>>)results.get("competition");

      competitionList.stream().forEach(ml ->{
        FootballCompetition footballCompetition = new FootballCompetition();
        Map<String,Object> competition = ml;
        Integer competitionId = (Integer)competition.get("id");
        String nameZh = (String)competition.get("name");
        String logo = (String)competition.get("logo");
        footballCompetition.setId(competitionId);
        footballCompetition.setNameZh(nameZh);
        footballCompetition.setLogo(logo);
        footballCompetition.setType(1);
        footballCompetitionDao.insert(footballCompetition);
      });
      matchList.stream().forEach(ml ->{
        FootballMatch footballMatch = new FootballMatch();
        Map<String,Object> matchMap = ml;
        Integer id = ((Integer)matchMap.get("id"));
        Integer seasonId = (Integer)matchMap.get("season_id");
        Integer competitionId = (Integer)matchMap.get("competition_id");
        Integer homeTeamId = (Integer)matchMap.get("home_team_id");
        Integer awayTeamId = (Integer)matchMap.get("away_team_id");
        Integer statusId = (Integer)matchMap.get("status_id");
        Long matchTime = Long.valueOf((Integer) matchMap.get("match_time"));
        Integer refereeId = (Integer)matchMap.get("referee_id");
        Map<String,Object> coverage = (Map<String,Object>)matchMap.get("coverage");
        Integer lineUp = 0;
        if(coverage != null && coverage.size() >0) {
          lineUp = (Integer)coverage.get("lineup");
        }
        Integer homeScore = 0;
        Integer awayScore = 0;
        //get home_scores from api
        List<Integer> homeScores = (List<Integer>)matchMap.get("home_scores");
        List<Integer> awayScores = (List<Integer>)matchMap.get("away_scores");
        if(homeScores != null && homeScores.size() >0) {
          //0:"比分(常规时间) - int"
          // 1:"半场比分 - int"
          // 2:"红牌 - int"
          // 3:"黄牌 - int"
          //4:"角球，-1表示没有角球数据 - int"
          //5:"加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int"
          // 6:"点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
          /* when score1 is equal 0, homeScore = score0 add score2, when score1 is unequal 0, homeScore = score1 + score2*/
          Integer score0 = homeScores.get(0);
          Integer score1 = homeScores.get(5);
          Integer score2 = homeScores.get(6);
          if(score1 != 0) {
            homeScore = score1 + score2;
          }else if(score1 == 0) {
            homeScore = score0 + score2;
          }
        }

        if(awayScores != null && awayScores.size() >0) {
          Integer score0 = awayScores.get(0);
          Integer score1 = awayScores.get(5);
          Integer score2 = awayScores.get(6);
          if(score1 != 0) {
            awayScore = score1 + score2;
          } else if(score1 ==0) {
            awayScore = score0 + score2;
          }
        }
        Long updatedTime = Long.valueOf((Integer)matchMap.get("updated_at"));
        footballMatch.setId(id);
        footballMatch.setSeasonId(seasonId);
        footballMatch.setCompetitionId(competitionId);
        footballMatch.setHomeTeamId(homeTeamId);
        footballMatch.setAwayTeamId(awayTeamId);
        footballMatch.setStatusId(statusId);
        footballMatch.setHomeTeamScore(homeScore);
        footballMatch.setAwayTeamScore(awayScore);
        footballMatch.setLineUp(lineUp);
        footballMatch.setMatchTime(matchTime);
        footballMatch.setUpdatedAt(updatedTime);
        footballMatch.setRefereeId(refereeId);
        FootballTeam homeTeam = footballTeamDao.getTeam(footballMatch.getHomeTeamId());
        if(homeTeam != null) {
          footballMatch.setHomeTeamName(homeTeam.getNameZh());
        }
        FootballTeam awayTeam = footballTeamDao.getTeam(footballMatch.getAwayTeamId());
        if(awayTeam != null) {
          footballMatch.setAwayTeamName(awayTeam.getNameZh());
        }
        footballMatchDao.insert(footballMatch);
      });
      return competitionList;
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


  public Map<String, Object> getMatchLineUp(Integer matchId) {
    String url = getNormalUrl(namiConfig.getFootballLineUpUrl());
    url += "&id="+matchId;
    String result = HttpUtil.getNaMiData(url);
    Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
    Integer code = (Integer) resultObj.get("code");
    if(code == 0) {
      Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
      if(results != null && results.size() >0){
        Integer confirmed = (Integer)results.get("confirmed");
        if(confirmed == 1) {

          // home team
          String homeFormation = (String)results.get("home_formation");
          String awayFormation= (String)results.get("away_formation");
          // update football formation
          footballMatchDao.updateFormation(homeFormation,awayFormation,matchId);
          List<Map<String,Object>> home = (List<Map<String, Object>>) results.get("home");
          List<Map<String,Object>> away = (List<Map<String, Object>>) results.get("away");
          if(home != null && home.size() >0) {
            home.stream().forEach(ml ->{
              HomeMatchLineUp homeMatchLineUp = getFootballHomeMatchLineUp(ml,matchId);
              HomeMatchLineUp homeMatchLineUpFromDb = homeMatchLineUpDao.getHomeMatchLineUp(homeMatchLineUp.getMatchId(),matchId);
              if(homeMatchLineUpFromDb == null) {
                homeMatchLineUpDao.insert(homeMatchLineUp);
              } else {
                homeMatchLineUpDao.updateHomeMatchLineUp(homeMatchLineUp);
              }

            });
          }
          if(away != null && away.size() >0) {
            away.stream().forEach(ml ->{
              AwayMatchLineUp awayMatchLineUp = getFootballAwayMatchLineUp(ml,matchId);
              AwayMatchLineUp awayMatchLineUpFromDb = awayMatchLineUpDao.getAwayMatchLineUp(awayMatchLineUp.getId(),matchId);
              if(awayMatchLineUpFromDb == null) {
                awayMatchLineUpDao.insert(awayMatchLineUp);
              } else {
                awayMatchLineUpDao.updateAwayMatchLineUp(awayMatchLineUp);
              }

            });
          }
        }
      }



    }
    return resultObj;
  }


  private HomeMatchLineUp getFootballHomeMatchLineUp(Map<String, Object> ml, Integer matchId) {
    Integer id = (Integer)ml.get("id");
    Integer teamId = (Integer)ml.get("team_id");
    Integer first = (Integer)ml.get("first");
    Integer captain = (Integer)ml.get("captain");
    String playerName = (String)ml.get("name");
    String playerLogo = (String)ml.get("logo");
    Integer shirtNumber = (Integer)ml.get("shirt_number");
    String position = (String)ml.get("position");
    String rating = (String)ml.get("rating");
    HomeMatchLineUp homeMatchLineUp = new HomeMatchLineUp();
    homeMatchLineUp.setId(id);
    homeMatchLineUp.setTeamId(teamId);
    homeMatchLineUp.setMatchId(matchId);
    homeMatchLineUp.setFirst(first);
    homeMatchLineUp.setCaptain(captain);
    homeMatchLineUp.setPlayerName(playerName);
    homeMatchLineUp.setPlayerLogo(playerLogo);
    homeMatchLineUp.setShirtNumber(shirtNumber);
    homeMatchLineUp.setPosition(position);
    homeMatchLineUp.setRating(rating);
    return homeMatchLineUp;
  }

  private AwayMatchLineUp getFootballAwayMatchLineUp(Map<String, Object> ml, Integer matchId) {
    Integer id = (Integer)ml.get("id");
    Integer teamId = (Integer)ml.get("team_id");
    Integer first = (Integer)ml.get("first");
    Integer captain = (Integer)ml.get("captain");
    String playerName = (String)ml.get("name");
    String playerLogo = (String)ml.get("logo");
    Integer shirtNumber = (Integer)ml.get("shirt_number");
    String position = (String)ml.get("position");
    String rating = (String)ml.get("rating");
    AwayMatchLineUp awayMatchLineUp = new AwayMatchLineUp();
    awayMatchLineUp.setId(id);
    awayMatchLineUp.setTeamId(teamId);
    awayMatchLineUp.setMatchId(matchId);
    awayMatchLineUp.setFirst(first);
    awayMatchLineUp.setCaptain(captain);
    awayMatchLineUp.setPlayerName(playerName);
    awayMatchLineUp.setPlayerLogo(playerLogo);
    awayMatchLineUp.setShirtNumber(shirtNumber);
    awayMatchLineUp.setPosition(position);
    awayMatchLineUp.setRating(rating);
    return awayMatchLineUp;
  }

}
