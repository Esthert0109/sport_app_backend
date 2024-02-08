package com.maindark.livestream.task;

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
import com.maindark.livestream.vo.FootballMatchLiveDataVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@Slf4j
@EnableScheduling
public class FootBallTask {

    private Integer maxMatchIdFromApi = 0;
    private Integer maxMatchTimeFromApi = 0;
    @Resource
    NamiConfig namiConfig;

    @Resource
    RedisService redisService;

    @Resource
    FootballTeamDao footballTeamDao;

    @Resource
    FootballMatchDao footballMatchDao;

    @Resource
    UpdateFootballDataDao updateFootballDataDao;

    @Resource
    HomeMatchLineUpDao homeMatchLineUpDao;

    @Resource
    AwayMatchLineUpDao awayMatchLineUpDao;

    @Resource
    FootballMatchLiveDataDao footballMatchLiveDataDao;

    @Resource
    FootballLiveAddressDao footballLiveAddressDao;

    @Resource
    FootballVenueDao footballVenueDao;
    @Resource
    FootballRefereeDao footballRefereeDao;





    /* execute every ten minutes every day*/
    @Scheduled(cron = "0 */30 * * * ?")
    //second, minute, hour, day of month, month, day(s) of week
    public void getAllMatchOrUpdate(){
        //Get all match
        while(true) {
            String url = namiConfig.getIdUrl(namiConfig.getFootballMatchUrl());
            if(maxMatchIdFromApi == 0) {
                Integer maxMatchIdFromDb = footballMatchDao.getMaxId();
                if(maxMatchIdFromDb == null) {
                    url += maxMatchIdFromApi +1;
                } else {
                    url += maxMatchIdFromDb + 1;
                }
            } else {
                url += maxMatchIdFromApi +1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer)resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxMatchIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> matchList = (List<Map<String,Object>>)resultObj.get("results");
                    if(matchList != null && !matchList.isEmpty()) {
                        int size = matchList.size();
                        for(int i =0;i<size;i++) {
                            FootballMatch footballMatch = insertOrUpdateMatch(matchList,i);
                            footballMatchDao.insert(footballMatch);
                            // put footballMatch into redis
//                            redisService.set(FootballMatchKey.matchKey,String.valueOf(footballMatch.getId()),footballMatch);
//                            // put footballMatchVo into redis
//                            FootballMatchVo footballMatchVo = footballMatchDao.getFootballMatchVoById(footballMatch.getId());
//                            footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(footballMatchVo.getStatusId()));
//                            footballMatchVo.setMatchTimeStr(DateUtil.interceptTime(footballMatchVo.getMatchTime()));
//                            // get referee name
//                            footballMatchVo.setRefereeName(getFootballRefereeName(footballMatch.getRefereeId()));
//                            // get venue name
//                            footballMatchVo.setVenueName(getFootballVenueName(footballMatch.getVenueId()));
//                            redisService.set(FootballMatchKey.matchVoKey,String.valueOf(footballMatch.getId()),footballMatchVo);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("get match data from nami are wrong, please connect to the nami platform!");
            }

        }

        // update match
        while(true) {
            String url = namiConfig.getTimeUrl(namiConfig.getFootballMatchUrl());
            if(maxMatchTimeFromApi == 0) {
                Integer maxUpdateAt = footballMatchDao.getMaxUpdatedAt();
                if(maxUpdateAt == null){
                    url += maxMatchTimeFromApi + 1;
                } else {
                    url += maxUpdateAt + 1;
                }
            } else {
                url += maxMatchTimeFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer)resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxMatchTimeFromApi = (Integer) query.get("max_time");
                if(total >0) {
                    List<Map<String,Object>> matchList = (List<Map<String,Object>>)resultObj.get("results");
                    if(matchList != null && !matchList.isEmpty()) {
                        int size = matchList.size();
                        for(int i =0;i<size;i++) {
                            FootballMatch footballMatch = insertOrUpdateMatch(matchList,i);
                            footballMatchDao.updateDataById(footballMatch);
                            // delete redis data
//                            redisService.delete(FootballMatchKey.matchKey,String.valueOf(footballMatch.getId()));
//                            redisService.delete(FootballMatchKey.matchVoKey,String.valueOf(footballMatch.getId()));
//                            // put data into redis
//                            redisService.set(FootballMatchKey.matchKey,String.valueOf(footballMatch.getId()),footballMatch);
//                            FootballMatchVo footballMatchVo = footballMatchDao.getFootballMatchVoById(footballMatch.getId());
//                            footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(footballMatchVo.getStatusId()));
//                            footballMatchVo.setMatchTimeStr(DateUtil.interceptTime(footballMatchVo.getMatchTime()));
//                            // get referee name
//                            footballMatchVo.setRefereeName(getFootballRefereeName(footballMatch.getRefereeId()));
//                            // get venue name
//                            footballMatchVo.setVenueName(getFootballVenueName(footballMatch.getVenueId()));
//                            redisService.set(FootballMatchKey.matchVoKey,String.valueOf(footballMatch.getId()),footballMatchVo);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("update match data from nami are wrong, please connect to the nami platform!");
            }

        }
    }







    /* execute this task every 20 seconds*/
    @Scheduled(cron = "*/30 * * * * ?")
    public void getUpdateDataFromNami(){
        String url = namiConfig.getNormalUrl(namiConfig.getUpdateDataUrl());
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
        if (code == null){
            log.error("nami error:{}",resultObj.get("err"));
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }
        if(code == 0) {
            Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
            if(!results.isEmpty()) {
                // if there is a key of 2, we can insert line-up data;
                if(results.containsKey("2")) {
                    List<Map<String,Object>> updateDataList = (List<Map<String,Object>>)results.get("2");
                    if(!updateDataList.isEmpty()) {
                        updateDataList.stream().forEach(el ->{
                            UpdateFootballData updateFootballData = getUpdateFootballData(el);
                            UpdateFootballData updateFootballDataFromData = updateFootballDataDao.getDataByUniqueKey(updateFootballData.getUniqueKey());
                            if(updateFootballDataFromData == null) {
                                updateFootballDataDao.insert(updateFootballData);
                                FootballMatch footballMatch = footballMatchDao.getFootballMatch(updateFootballData.getMatchId());
                                if(footballMatch != null) {
                                    Integer lineUp = footballMatch.getLineUp();
                                    if(lineUp == 1) {
                                        setMatchLineUp(updateFootballData.getMatchId());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        } else {
            log.error("there is no update data!");
        }
    }


    /* execute this task every two seconds*/
    @Scheduled(cron = "*/30 * * * * ?")
    public void getMatchResult(){
        String url = namiConfig.getNormalUrl(namiConfig.getFootballMatchLiveUrl());
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
        if (code == null){
            log.error("nami error:{}",resultObj.get("err"));
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }
        if(code == 0)
        {
            List<Map<String,Object>> results = (List<Map<String,Object>>)resultObj.get("results");
            if(results != null && !results.isEmpty())
            {
               int size = results.size();
               for(int i=0;i<size;i++)
               {
                   Map<String,Object> ml = results.get(i);
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
                       Integer homePenaltyNum = 0;
                       Integer awayPenaltyNum = 0;
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
                               } else {
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
                               }else{
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
                                  case 8:
                                      homePenaltyNum = home;
                                      awayPenaltyNum = away;
                              }
                          }
                       }

                       FootballMatch footballMatch = redisService.get(FootballMatchKey.matchKey,String.valueOf(matchId),FootballMatch.class);
                       if(footballMatch == null) {
                           footballMatch = footballMatchDao.getFootballMatchById(matchId);
                       }
                       if(matchStatus != 0) {
                           footballMatch.setStatusId(matchStatus);
                       }
                       if(homeScore != 0) {
                           footballMatch.setHomeTeamScore(homeScore);
                       }
                       if(awayScore !=0) {
                           footballMatch.setAwayTeamScore(awayScore);
                       }

                        // update table football_match
                        footballMatchDao.updateDataById(footballMatch);
                       // update redis cache
                        redisService.set(FootballMatchKey.matchKey,String.valueOf(matchId),footballMatch);
                        FootballMatchVo footballMatchVo = footballMatchDao.getFootballMatchVoById(footballMatch.getId());
                        footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(footballMatchVo.getStatusId()));
                        footballMatchVo.setMatchTimeStr(DateUtil.interceptTime(footballMatchVo.getMatchTime()));
                        // get referee name
                        footballMatchVo.setRefereeName(getFootballRefereeName(footballMatch.getRefereeId()));
                        // get venue name
                        footballMatchVo.setVenueName(getFootballVenueName(footballMatch.getVenueId()));
                        redisService.set(FootballMatchKey.matchVoKey,String.valueOf(footballMatch.getId()),footballMatchVo);

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
                           footballMatchLiveData.setHomeTeamId(footballMatch.getHomeTeamId());
                           footballMatchLiveData.setAwayTeamId(footballMatch.getAwayTeamId());
                           footballMatchLiveData.setHomeTeamName(footballMatch.getHomeTeamName());
                           footballMatchLiveData.setAwayTeamName(footballMatch.getAwayTeamName());
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
                           footballMatchLiveData.setHomePenaltyNum(homePenaltyNum);
                           footballMatchLiveData.setAwayPenaltyNum(awayPenaltyNum);
                           footballMatchLiveDataDao.insert(footballMatchLiveData);
                       }
                   } else {
                       footballMatchLiveData.setMatchId(matchId);
                       footballMatchLiveData.setStatusId(matchStatus);
                       footballMatchLiveData.setHomeScore(homeScore);
                       footballMatchLiveData.setAwayScore(awayScore);
                       footballMatchLiveData.setHomeTeamId(footballMatch.getHomeTeamId());
                       footballMatchLiveData.setAwayTeamId(footballMatch.getAwayTeamId());
                       footballMatchLiveData.setHomeTeamName(footballMatch.getHomeTeamName());
                       footballMatchLiveData.setAwayTeamName(footballMatch.getAwayTeamName());
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
                       footballMatchLiveData.setHomePenaltyNum(homePenaltyNum);
                       footballMatchLiveData.setAwayPenaltyNum(awayPenaltyNum);
                       // delete redis data and reset data later
                       redisService.delete(FootballMatchKey.matchLiveKey,String.valueOf(matchId));
                       redisService.delete(FootballMatchKey.matchLiveVoKey,String.valueOf(matchId));
                       footballMatchLiveDataDao.updateFootballMatchLiveData(footballMatchLiveData);
                   }
                   // put footballMatchLiveData into redis
                   FootballMatchLiveDataVo footballMatchLiveDataVo = footballMatchLiveDataDao.getFootballMatchLiveDataVo(matchId);
                   redisService.set(FootballMatchKey.matchLiveKey,String.valueOf(matchId),footballMatchLiveData);
                   redisService.set(FootballMatchKey.matchLiveVoKey,String.valueOf(matchId),footballMatchLiveDataVo);
               }
            }
        }
    }


    /* Get live url*/
    @Scheduled(cron = " 0 */20 * * * ?")
    public void getLiveUrlAddress(){
        String url = namiConfig.getFootballLiveAddress(namiConfig.getFootballLiveAddress());
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
        if (code == null){
            log.error("nami error:{}",resultObj.get("err"));
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }
        if(code ==0){
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
    }



    private void setMatchLineUp(Integer matchId){
        String url = namiConfig.getNormalUrl(namiConfig.getFootballLineUpUrl());
        url += "&id="+matchId;
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
        if(code == 0) {
            Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
            if(results!= null && !results.isEmpty()) {
                Integer confirmed = (Integer)results.get("confirmed");
                // 正式阵容，1-是、0-不是
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
                            HomeMatchLineUp homeMatchLineUpFromDb = homeMatchLineUpDao.getHomeMatchLineUp(homeMatchLineUp.getId(),matchId);
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
        Integer x = (Integer)ml.get("x");
        Integer y = (Integer)ml.get("y");
        HomeMatchLineUp homeMatchLineUp = new HomeMatchLineUp();
        homeMatchLineUp.setPlayerId(id);
        homeMatchLineUp.setTeamId(teamId);
        homeMatchLineUp.setMatchId(matchId);
        homeMatchLineUp.setFirst(first);
        homeMatchLineUp.setCaptain(captain);
        homeMatchLineUp.setPlayerName(playerName);
        homeMatchLineUp.setPlayerLogo(playerLogo);
        homeMatchLineUp.setShirtNumber(shirtNumber);
        homeMatchLineUp.setPosition(position);
        homeMatchLineUp.setRating(rating);
        homeMatchLineUp.setX(x);
        homeMatchLineUp.setY(y);
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
        Integer x = (Integer)ml.get("x");
        Integer y = (Integer)ml.get("y");
        AwayMatchLineUp awayMatchLineUp = new AwayMatchLineUp();
        awayMatchLineUp.setPlayerId(id);
        awayMatchLineUp.setTeamId(teamId);
        awayMatchLineUp.setMatchId(matchId);
        awayMatchLineUp.setFirst(first);
        awayMatchLineUp.setCaptain(captain);
        awayMatchLineUp.setPlayerName(playerName);
        awayMatchLineUp.setPlayerLogo(playerLogo);
        awayMatchLineUp.setShirtNumber(shirtNumber);
        awayMatchLineUp.setPosition(position);
        awayMatchLineUp.setRating(rating);
        awayMatchLineUp.setX(x);
        awayMatchLineUp.setY(y);
        return awayMatchLineUp;
    }

    private FootballMatch insertOrUpdateMatch(List<Map<String, Object>> list, int i) {
        FootballMatch footballMatch = new FootballMatch();
        Map<String,Object> matchMap = list.get(i);
        Integer id = ((Integer)matchMap.get("id"));
        Integer seasonId = (Integer)matchMap.get("season_id");
        Integer competitionId = (Integer)matchMap.get("competition_id");
        Integer homeTeamId = (Integer)matchMap.get("home_team_id");
        Integer awayTeamId = (Integer)matchMap.get("away_team_id");
        Integer statusId = (Integer)matchMap.get("status_id");
        Long matchTime = Long.valueOf((Integer) matchMap.get("match_time"));
        Integer refereeId =  (Integer) matchMap.get("referee_id");
        Integer venueId =  (Integer) matchMap.get("venue_id");
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
            } else {
                homeScore = score0 + score2;
            }
        }

        if(awayScores != null && awayScores.size() >0) {
            Integer score0 = awayScores.get(0);
            Integer score1 = awayScores.get(5);
            Integer score2 = awayScores.get(6);
            if(score1 != 0) {
                awayScore = score1 + score2;
            } else {
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
        FootballTeam homeTeam = footballTeamDao.getTeam(footballMatch.getHomeTeamId());
        if(homeTeam != null) {
            footballMatch.setHomeTeamName(homeTeam.getNameZh());
            footballMatch.setHomeTeamLogo(homeTeam.getLogo());
        }
        FootballTeam awayTeam = footballTeamDao.getTeam(footballMatch.getAwayTeamId());
        if(awayTeam != null) {
            footballMatch.setAwayTeamLogo(awayTeam.getLogo());
            footballMatch.setAwayTeamName(awayTeam.getNameZh());
        }
        footballMatch.setRefereeId(refereeId);
        footballMatch.setVenueId(venueId);
        return footballMatch;
    }

    private UpdateFootballData getUpdateFootballData(Map<String,Object> el){
        Integer matchId = (Integer)el.get("match_id");
        Integer seasonId = (Integer)el.get("season_id");
        Integer competitionId = (Integer)el.get("competition_id");
        Integer pubTime = (Integer)el.get("pub_time");
        Long updateTime = Long.valueOf((Integer)el.get("update_time"));
        Long uniqueKey = matchId + seasonId + competitionId + pubTime + updateTime;
        UpdateFootballData updateFootballData = new UpdateFootballData();
        updateFootballData.setMatchId(matchId);
        updateFootballData.setSeasonId(seasonId);
        updateFootballData.setCompetitionId(competitionId);
        updateFootballData.setPubTime(pubTime);
        updateFootballData.setUpdateTime(updateTime);
        updateFootballData.setUniqueKey(uniqueKey);
        return updateFootballData;
    }

    private FootballLiveAddress getFootballLiveAddress(Map<String,Object> m){
        Integer sportId = (Integer)m.get("sport_id");
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
        footballLiveAddress.setSportId(sportId);
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

    public String getFootballRefereeName(Integer refereeId){
        FootballReferee footballReferee = footballRefereeDao.getFootballRefereeById(refereeId);
        return footballReferee.getNameZh();
    }

    public String getFootballVenueName(Integer venueId){
        FootballVenue footballVenue = footballVenueDao.getFootballVenueById(venueId);
        return footballVenue.getNameZh();
    }

}
