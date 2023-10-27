package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.*;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.FootballMatchStatus;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.vo.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  @Resource
  RedisService redisService;

  @Resource
  FootballMatchLiveDataDao footballMatchLiveDataDao;

  @Resource
  FootballLiveAddressDao footballLiveAddressDao;

  @Resource
  FootballCoachDao footballCoachDao;




  public String getNormalUrl(String normalUrl){
    return namiConfig.getHost() + normalUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey();
  }

    public List<FootballMatchVo> getFootBallMatchList(String competitionName, String teamName,Pageable pageable){
      long offset = pageable.getOffset();
      Integer limit = pageable.getPageSize();
      List<FootballMatchVo> list = null;
      if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
        throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
      }
      LocalDate now = LocalDate.now();
      LocalDate tomorrow = now.plusDays(1);
      long nowSeconds = DateUtil.convertDateToLongTime(now);
      long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
      if(!StringUtils.isBlank(competitionName)){
          list = footballMatchDao.getFootballMatchByCompetitionName(competitionName,nowSeconds,tomorrowSeconds,limit,offset);
      } else if(!StringUtils.isBlank(teamName)){
          list = footballMatchDao.getFootballMatchByTeamName(teamName,nowSeconds,tomorrowSeconds,limit,offset);
      }
      if(list != null && !list.isEmpty()){
        int size = list.size();
        for(int i=0;i<size;i++) {
          FootballMatchVo footballMatchVo = list.get(i);
          Long matchTime = footballMatchVo.getMatchTime() * 1000;
          String timeStr = DateUtil.interceptTime(matchTime);
          footballMatchVo.setMatchTimeStr(timeStr);
          footballMatchVo.setMatchDate(DateUtil.convertLongTimeToMatchDate(matchTime));
          Integer statusId = footballMatchVo.getStatusId();
          footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
        }
      }
      return list;
    }



  public Map<String,List<FootballMatchVo>> getFootballMatchesStarts(Pageable pageable) {
    Long offset = pageable.getOffset();
    Integer limit = pageable.getPageSize();
    // get all today's start matches
    LocalDate now = LocalDate.now();
    Long nowSeconds = DateUtil.convertDateToLongTime(now);
    LocalDate tomorrow = now.plusDays(1);
    Long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
    List<FootballMatchVo> startMatches = footballMatchDao.getFootballMatchesStart(nowSeconds,tomorrowSeconds,limit,offset);
    startMatches = getFootballMatchVos(startMatches);
    Map<String,List<FootballMatchVo>> results = new HashMap<>();
    results.put("start",startMatches);
    return results;
  }

  public Map<String,List<FootballMatchVo>> getFootballMatchesPasts(Pageable pageable) {
    Long offset = pageable.getOffset();
    Integer limit = pageable.getPageSize();
    // get all past matches
    LocalDate now = LocalDate.now();
    Long nowSeconds = DateUtil.convertDateToLongTime(now);
    LocalDate past = now.minusDays(6);
    Long pastSeconds = DateUtil.convertDateToLongTime(past);
    List<FootballMatchVo> pastMatches = footballMatchDao.getFootballMatchesPast(pastSeconds,nowSeconds,limit,offset);
    pastMatches = getFootballMatchVos(pastMatches);
    Map<String,List<FootballMatchVo>> results = new HashMap<>();
    results.put("pass",pastMatches);
    return results;
  }

  public Map<String,List<FootballMatchVo>> getFootballMatchesFuture(Pageable pageable) {
    Long offset = pageable.getOffset();
    Integer limit = pageable.getPageSize();
    // get all future matches in seven days
    LocalDate now = LocalDate.now();
    Long nowSeconds = DateUtil.convertDateToLongTime(now);
    LocalDate future = now.plusDays(6);
    Long futureSeconds = DateUtil.convertDateToLongTime(future);
    List<FootballMatchVo> futureMatches = footballMatchDao.getFootballMatchesFuture(nowSeconds,futureSeconds,limit,offset);
    futureMatches = getFootballMatchVos(futureMatches);
    Map<String,List<FootballMatchVo>> results = new HashMap<>();
    results.put("future",futureMatches);
    return results;
  }


    /*
    *
    * get all matches in seven days
    * */
  public Map<String,List<FootballMatchVo>> getFootballMatchesInSevenDays(Pageable pageable) {
    Long offset = pageable.getOffset();
    Integer limit = pageable.getPageSize();
    // get all start matches
    LocalDate now = LocalDate.now();
    Long nowSeconds = DateUtil.convertDateToLongTime(now);
   // Map<String,List<FootballMatchVo>> results = redisService.get(FootballListKey.listKey,String.valueOf(nowSeconds),Map.class);
   // if (results == null) {
      LocalDate tomorrow = now.plusDays(1);
      LocalDate future = now.plusDays(6);
      LocalDate past = now.minusDays(6);
      Long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
      Long futureSeconds = DateUtil.convertDateToLongTime(future);
      Long pastSeconds = DateUtil.convertDateToLongTime(past);
      log.info("get past date:{},now date:{},tomorrow date:{},future date:{}",pastSeconds,nowSeconds,tomorrowSeconds,futureSeconds);
      List<FootballMatchVo> pastMatches = footballMatchDao.getFootballMatchesPast(pastSeconds,nowSeconds,limit,offset);
      pastMatches = getFootballMatchVos(pastMatches);
      List<FootballMatchVo> startMatches = footballMatchDao.getFootballMatchesStart(nowSeconds,tomorrowSeconds,limit,offset);
      startMatches = getFootballMatchVos(startMatches);
      List<FootballMatchVo> futureMatches = footballMatchDao.getFootballMatchesFuture(nowSeconds,futureSeconds,limit,offset);
      futureMatches = getFootballMatchVos(futureMatches);
      Map<String,List<FootballMatchVo>> results = new HashMap<>();
      results.put("pass",pastMatches);
      results.put("start",startMatches);
      results.put("future",futureMatches);
      //redisService.set(FootballListKey.listKey,String.valueOf(nowSeconds),results);
   // }
    return results;
  }

  private List<FootballMatchVo> getFootballMatchVos(List<FootballMatchVo> futureMatches) {
    if(futureMatches != null && !futureMatches.isEmpty()){
      Stream<FootballMatchVo> footballMatchVoStream = futureMatches.stream().map(vo ->{
        vo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(vo.getStatusId()));
        vo.setMatchTimeStr(DateUtil.interceptTime(vo.getMatchTime() * 1000));
        vo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(vo.getStatusId()));
        vo.setMatchDate(DateUtil.convertLongTimeToMatchDate(vo.getMatchTime() * 1000));
        return vo;
      });
      futureMatches = getArrayListFromStream(footballMatchVoStream);
    }
    return futureMatches;
  }

  public List<FootballMatchVo> getMatchListByDate(String date,Pageable pageable,String checkData) {
    long offset = pageable.getOffset();
    Integer limit = pageable.getPageSize();
    List<FootballMatchVo> footballMatchVos = null;
    // query today's matches do not start
    if(StringUtils.equals("true",checkData)){
      LocalDate now = LocalDate.now();
      Long nowSeconds = DateUtil.convertDateToLongTime(now);
      LocalDate tomorrow = now.plusDays(1);
      Long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
      footballMatchVos = footballMatchDao.getFootballMatchNotStart(nowSeconds,tomorrowSeconds,limit,offset);
      footballMatchVos = getFootballMatchVos(footballMatchVos);
    } else if(StringUtils.equals("false",checkData)) {
      LocalDate currentDate = DateUtil.convertStringToDate(date);
      LocalDate deadline = currentDate.plusDays(1);
      Long currentSeconds = DateUtil.convertDateToLongTime(currentDate);
      Long deadlineSeconds = DateUtil.convertDateToLongTime(deadline);
      footballMatchVos = footballMatchDao.getFootballMatchFinished(currentSeconds,deadlineSeconds,limit,offset);
      footballMatchVos = getFootballMatchVos(footballMatchVos);
    } else {
      LocalDate currentDate = DateUtil.convertStringToDate(date);
      LocalDate deadline = currentDate.plusDays(1);
      Long currentSeconds = DateUtil.convertDateToLongTime(currentDate);
      Long deadlineSeconds = DateUtil.convertDateToLongTime(deadline);
      footballMatchVos = footballMatchDao.getFootballMatchByDate(currentSeconds,deadlineSeconds,limit,offset);
      footballMatchVos = getFootballMatchVos(footballMatchVos);
    }
    return footballMatchVos;
  }


  public FootballMatchLiveDataVo getMatchLiveData(Integer matchId){
    FootballMatchLiveDataVo footballMatchLiveDataVo = redisService.get(FootballMatchKey.matchLiveVoKey,String.valueOf(matchId),FootballMatchLiveDataVo.class);
    if(footballMatchLiveDataVo == null) {
      footballMatchLiveDataVo = footballMatchLiveDataDao.getFootballMatchLiveDataVo(matchId);
      Integer homeTeamId = footballMatchLiveDataVo.getHomeTeamId();
      Integer awayTeamId = footballMatchLiveDataVo.getAwayTeamId();
      FootballCoach homeCoach = footballCoachDao.getCoachByTeamId(homeTeamId);
      if(homeCoach != null) {
        footballMatchLiveDataVo.setHomeCoach(homeCoach.getNameZh());
      }
      FootballCoach awayCoach = footballCoachDao.getCoachByTeamId(awayTeamId);
      if(awayCoach != null) {
        footballMatchLiveDataVo.setAwayCoach(awayCoach.getNameZh());
      }
    }
    return footballMatchLiveDataVo;
  }

  public List<Map<String,Object>> getMatchList(String competitionDate){
      String url = getNormalUrl(namiConfig.getFootballUrl())+"&date="+ competitionDate;
      String result = HttpUtil.getNaMiData(url);
      Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
      String msg = (String)resultObj.get("err");
      if(msg != null){
        throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
      }
      Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
      List<Map<String,Object>> matchList = (List<Map<String,Object>>)results.get("match");
      List<Map<String,Object>> competitionList = (List<Map<String,Object>>)results.get("competition");
      List<Map<String,Object>> teamList = (List<Map<String,Object>>)results.get("team");

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
        FootballCompetition footballCompetitionFromDb = footballCompetitionDao.getFootballCompetitionById(competitionId);
        if(footballCompetitionFromDb == null) {
          footballCompetitionDao.insert(footballCompetition);
        }

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
        if(coverage != null && !coverage.isEmpty()) {
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

        if(awayScores != null && !awayScores.isEmpty()) {
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
          footballMatch.setHomeTeamLogo(homeTeam.getLogo());
        }
        FootballTeam awayTeam = footballTeamDao.getTeam(footballMatch.getAwayTeamId());
        if(awayTeam != null) {
          footballMatch.setAwayTeamName(awayTeam.getNameZh());
          footballMatch.setAwayTeamLogo(awayTeam.getLogo());
        }
        FootballMatch footballMatchFromDb = footballMatchDao.getFootballMatchById(id);
        if(footballMatchFromDb == null) {
          footballMatchDao.insert(footballMatch);
        }

      });
      teamList.stream().forEach(ml ->{
        FootballTeam footballTeam = new FootballTeam();
        Integer teamId = (Integer)ml.get("id");
        String name = (String)ml.get("name");
        String logo = (String)ml.get("logo");
        footballTeam.setId(teamId);
        footballTeam.setNameZh(name);
        footballTeam.setLogo(logo);
        footballTeam.setCompetitionId(275);
        FootballTeam footballTeamFromDb = footballTeamDao.getTeam(teamId);
        if(footballTeamFromDb == null) {
          footballTeamDao.insert(footballTeam);
        }
      });
      return competitionList;
    }


    public FootballMatchLineUpVo getFootballMatchLineUpByMatchId(Integer matchId){
      FootballMatchLineUpVo footballMatchLineUpVo = redisService.get(FootballMatchKey.matchLineUpKey,String.valueOf(matchId),FootballMatchLineUpVo.class);
      if(footballMatchLineUpVo == null) {
        footballMatchLineUpVo = new FootballMatchLineUpVo();
        List<HomeMatchLineUp> homeMatchLineUpList = homeMatchLineUpDao.getHomeMatchLineUpByMatchId(matchId);
        List<AwayMatchLineUp> awayMatchLineUpList = awayMatchLineUpDao.getAwayMatchLineUpByMatchId(matchId);
        footballMatchLineUpVo.setHomeMatchLineUpList(homeMatchLineUpList);
        footballMatchLineUpVo.setAwayMatchLineList(awayMatchLineUpList);
        redisService.set(FootballMatchKey.matchLineUpKey,String.valueOf(matchId),footballMatchLineUpVo);
      }
      return footballMatchLineUpVo;
    }

    public FootballLiveAddressVo getFootballLiveAddressByMatchId(Integer matchId){
      return footballLiveAddressDao.getLiveAddressByMatchId(matchId);
    }

  public Map<String, Object> getMatchLineUp(Integer matchId) {
    String url = namiConfig.getNormalUrl(namiConfig.getFootballLineUpUrl());
    url += "&id="+matchId;
    String result = HttpUtil.getNaMiData(url);
    Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
    log.info("nami result:{}",result);
    Integer code = (Integer) resultObj.get("code");
    if (code == null){
      return resultObj;
    }
    if(0 == code) {
      Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
      if(results != null && !results.isEmpty()){
        Integer confirmed = (Integer)results.get("confirmed");
        if(confirmed == 1) {

          // home team
          String homeFormation = (String)results.get("home_formation");
          String awayFormation= (String)results.get("away_formation");
          // update football formation
          footballMatchDao.updateFormation(homeFormation,awayFormation,matchId);
          List<Map<String,Object>> home = (List<Map<String, Object>>) results.get("home");
          List<Map<String,Object>> away = (List<Map<String, Object>>) results.get("away");
          if(home != null && !home.isEmpty()) {
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
          if(away != null && !away.isEmpty()) {
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

  public Map<String, Object> getMatchLive(Integer s) {
    String url = getNormalUrl(namiConfig.getFootballMatchLiveUrl());
    String result = HttpUtil.getNaMiData(url);
    Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
    Integer code = (Integer) resultObj.get("code");
    if (code == null){
      return resultObj;
    }
    if(code == 0)
    {
      List<Map<String,Object>> results = (List<Map<String,Object>>)resultObj.get("results");
      if(results != null && !results.isEmpty())
      {
        int size = results.size();
        for(int i=0;i<size;i++)
        {
          Map<String,Object> ml = (Map<String,Object>)results.get(i);
          Integer matchStatus = 0;
          Integer homeScore = 0;
          Integer awayScore = 0;
          Integer homeAttackNum = 0;
          Integer awayAttackNum = 0;
          Integer homeAttackDangerNum = 0;
          Integer awayAttackDangerNum = 0;
          Integer homePossessionRate = 0;
          Integer awayPossessionRate = 0;
          Integer homeShootGoalNum = 0;
          Integer awayShootGoalNum = 0;
          Integer homeBiasNum = 0;
          Integer awayBiasNum = 0;
          Integer homeCornerKickNum = 0;
          Integer awayCornerKickNum = 0;
          Integer homeRedCardNum = 0;
          Integer awayRedCardNum = 0;
          Integer homeYellowCardNum = 0;
          Integer awayYellowCardNum = 0;
          Integer matchId = (Integer)ml.get("id");
          List<Object> score = (List<Object>) ml.get("score");
          if(score != null && !score.isEmpty())
          {
            matchStatus = (Integer)score.get(1);
            List<Integer> homeScores = (List<Integer>) score.get(2);
            if(homeScores != null && !homeScores.isEmpty()) {
              Integer score0 = homeScores.get(0);
              Integer score1 = homeScores.get(5);
              Integer score2 = homeScores.get(6);
              if(score1 != 0) {
                homeScore = score1 + score2;
              }else if(score1 == 0) {
                homeScore = score0 + score2;
              }

            }
            List<Integer> awayScores = (List<Integer>) score.get(3);
            if(awayScores != null && !awayScores.isEmpty()) {
              Integer score0 = awayScores.get(0);
              Integer score1 = awayScores.get(5);
              Integer score2 = awayScores.get(6);
              if(score1 != 0) {
                awayScore = score1 + score2;
              }else if(score1 == 0) {
                awayScore = score0 + score2;
              }
            }
          }

          List<Map<String,Object>> statsList = (List<Map<String,Object>>)ml.get("stats");
          if(statsList != null && !statsList.isEmpty())
          {
            int statSize = statsList.size();
            for(int j=0;j<statSize;j++)
            {
              Map<String,Object> mp = statsList.get(j);
              Integer type = (Integer)mp.get("type");
              Integer home = (Integer)mp.get("home");
              Integer away = (Integer)mp.get("away");
              switch (type)
              {
                // attackNum
                case 23:
                  homeAttackNum = home;
                  awayAttackNum = away;
                  break;
                case 24:
                  homeAttackDangerNum = home;
                  awayAttackDangerNum = away;
                  break;
                case 25:
                  homePossessionRate = home;
                  awayPossessionRate = away;
                  break;
                case 21:
                  homeShootGoalNum = home;
                  awayShootGoalNum = away;
                  break;
                case 22:
                  homeBiasNum = home;
                  awayBiasNum = away;
                  break;
                case 2:
                  homeCornerKickNum = home;
                  awayCornerKickNum = away;
                  break;
                case 3:
                  homeYellowCardNum = home;
                  awayYellowCardNum = away;
                  break;
                case 4:
                  homeRedCardNum = home;
                  awayRedCardNum = away;
                  break;
              }
            }
          }

          // football_match_live_data
          FootballMatchLiveData footballMatchLiveData = redisService.get(FootballMatchKey.matchLiveKey,String.valueOf(matchId),FootballMatchLiveData.class);
          if(footballMatchLiveData == null) {
            FootballMatchLiveData footballMatchLiveDataFromDb = footballMatchLiveDataDao.getFootballMatchLiveData(matchId);
            if(footballMatchLiveDataFromDb == null){
              footballMatchLiveData = new FootballMatchLiveData();
              footballMatchLiveData.setMatchId(matchId);
              footballMatchLiveData.setStatusId(matchStatus);
              footballMatchLiveData.setHomeScore(homeScore);
              footballMatchLiveData.setAwayScore(awayScore);
              footballMatchLiveData.setHomeTeamId(0);
              footballMatchLiveData.setAwayTeamId(0);
              footballMatchLiveData.setHomeTeamName("homeTeam");
              footballMatchLiveData.setAwayTeamName("awayTeam");
              footballMatchLiveData.setHomeAttackNum(homeAttackNum);
              footballMatchLiveData.setAwayAttackNum(awayAttackNum);
              footballMatchLiveData.setHomeAttackDangerNum(homeAttackDangerNum);
              footballMatchLiveData.setAwayAttackDangerNum(awayAttackDangerNum);
              footballMatchLiveData.setHomeBiasNum(homeBiasNum);
              footballMatchLiveData.setAwayBiasNum(awayBiasNum);
              footballMatchLiveData.setHomePossessionRate(homePossessionRate);
              footballMatchLiveData.setAwayPossessionRate(awayPossessionRate);
              footballMatchLiveData.setHomeCornerKickNum(homeCornerKickNum);
              footballMatchLiveData.setAwayCornerKickNum(awayCornerKickNum);
              footballMatchLiveData.setHomeShootGoalNum(homeShootGoalNum);
              footballMatchLiveData.setAwayShootGoalNum(awayShootGoalNum);
              footballMatchLiveData.setHomeYellowCardNum(homeYellowCardNum);
              footballMatchLiveData.setAwayYellowCardNum(awayYellowCardNum);
              footballMatchLiveData.setHomeRedCardNum(homeRedCardNum);
              footballMatchLiveData.setAwayRedCardNum(awayRedCardNum);
              footballMatchLiveDataDao.insert(footballMatchLiveData);
            }
          } else {
            footballMatchLiveData.setMatchId(matchId);
            footballMatchLiveData.setStatusId(matchStatus);
            footballMatchLiveData.setHomeScore(homeScore);
            footballMatchLiveData.setAwayScore(awayScore);
            footballMatchLiveData.setHomeTeamId(0);
            footballMatchLiveData.setAwayTeamId(0);
            footballMatchLiveData.setHomeTeamName("homeTeam");
            footballMatchLiveData.setAwayTeamName("awayTeam");
            footballMatchLiveData.setHomeAttackNum(homeAttackNum);
            footballMatchLiveData.setAwayAttackNum(awayAttackNum);
            footballMatchLiveData.setHomeAttackDangerNum(homeAttackDangerNum);
            footballMatchLiveData.setAwayAttackDangerNum(awayAttackDangerNum);
            footballMatchLiveData.setHomeBiasNum(homeBiasNum);
            footballMatchLiveData.setAwayBiasNum(awayBiasNum);
            footballMatchLiveData.setHomePossessionRate(homePossessionRate);
            footballMatchLiveData.setAwayPossessionRate(awayPossessionRate);
            footballMatchLiveData.setHomeCornerKickNum(homeCornerKickNum);
            footballMatchLiveData.setAwayCornerKickNum(awayCornerKickNum);
            footballMatchLiveData.setHomeShootGoalNum(homeShootGoalNum);
            footballMatchLiveData.setAwayShootGoalNum(awayShootGoalNum);
            footballMatchLiveData.setHomeYellowCardNum(homeYellowCardNum);
            footballMatchLiveData.setAwayYellowCardNum(awayYellowCardNum);
            footballMatchLiveData.setHomeRedCardNum(homeRedCardNum);
            footballMatchLiveData.setAwayRedCardNum(awayRedCardNum);

            // delete redis data and reset data later
            redisService.delete(FootballMatchKey.matchLiveKey,String.valueOf(matchId));
            footballMatchLiveDataDao.updateFootballMatchLiveData(footballMatchLiveData);
          }
          // put footballMatchLiveData into redis
          redisService.set(FootballMatchKey.matchLiveKey,String.valueOf(matchId),footballMatchLiveData);
        }
      }
    }
     return null;
  }

  public Map<String,Object> getLiveAddress() {
    String url = namiConfig.getFootballLiveAddress() + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey();
    String result = HttpUtil.getNaMiData(url);
    Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
    Integer code = (Integer) resultObj.get("code");
    if (code != null) {
      List<Map<String,Object>> results = (List<Map<String,Object>>)resultObj.get("data");
      if(results != null && !results.isEmpty()){
        results.stream().forEach(m ->{
          FootballLiveAddress footballLiveAddress = getFootballLiveAddress(m);
          FootballLiveAddress footballLiveAddressFromDb = footballLiveAddressDao.getFootballLiveAddressByMatchId(footballLiveAddress.getMatchId());
          if(footballLiveAddressFromDb == null){
            footballLiveAddressDao.insert(footballLiveAddress);
          } else {
            footballLiveAddressDao.updateFootballLiveAddressByMatchId(footballLiveAddress);
          }
        });
      }
    }
    return resultObj;
  }


  public static <T> ArrayList<T>
  getArrayListFromStream(Stream<T> stream)
  {
    List<T> list = stream.collect(Collectors.toList());
    // Create an ArrayList of the List
    ArrayList<T> arrayList = new ArrayList<T>(list);
    // Return the ArrayList
    return arrayList;
  }

  private FootballLiveAddress getFootballLiveAddress(Map<String,Object> m){
    Integer id = (Integer)m.get("sport_id");
    Integer matchId = (Integer)m.get("match_id");
    Long matchTime = Long.valueOf((Integer) m.get("match_time"));
    Integer matchStatus = (Integer)m.get("match_status");
    Integer compId = (Integer)m.get("comp_id");
    String comp = (String)m.get("comp");
    String homeTeam = (String)m.get("home");
    String awayTeam = (String)m.get("away");
    String pushUrl1 = (String)m.get("pushurl1");
    String pushUrl2 = (String)m.get("pushurl2");
    String pushUrl3 = (String)m.get("pushurl3");
    FootballLiveAddress footballLiveAddress = new FootballLiveAddress();
    footballLiveAddress.setSportId(id);
    footballLiveAddress.setMatchId(matchId);
    footballLiveAddress.setMatchTime(matchTime);
    footballLiveAddress.setMatchStatus(matchStatus);
    footballLiveAddress.setCompId(compId);
    footballLiveAddress.setComp(comp);
    footballLiveAddress.setHomeTeam(homeTeam);
    footballLiveAddress.setAwayTeam(awayTeam);
    footballLiveAddress.setPushUrl1(pushUrl1);
    footballLiveAddress.setPushUrl2(pushUrl2);
    footballLiveAddress.setPushUrl3(pushUrl3);
    return footballLiveAddress;
  }



}
