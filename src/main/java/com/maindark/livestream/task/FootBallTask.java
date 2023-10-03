package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.*;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class FootBallTask {

    private Integer maxCompetitionIdFromApi = 0;

    private Integer maxCompetitionTimeFromApi = 0;

    private Integer maxTeamIdFromApi = 0;
    private Integer maxTeamTimeFromApi = 0;

    private Integer maxMatchIdFromApi = 0;
    private Integer maxMatchTimeFromApi = 0;

    @Resource
    NamiConfig namiConfig;

    @Resource
    RedisService redisService;

    @Resource
    FootballCompetitionDao footballCompetitionDao;

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



    public String getIdUrl(String idUrl){
       String url =  namiConfig.getHost() + idUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&id=";
        return url;
    }

    public String getTimeUrl(String timeUrl){
        String url = namiConfig.getHost() + timeUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&time=";
        return url;
    }

    public String getNormalUrl(String normalUrl){
        String url = namiConfig.getHost() + normalUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey();
        return url;
    }

    /* execute every ten minutes every day*/
    //@Scheduled(cron = "0 */10 * * * ?")
    //second, minute, hour, day of month, month, day(s) of week
    public void getAllMatchOrUpdate(){
        //Get all match
        while(true) {
            String url = getIdUrl(namiConfig.getFootballMatchUrl());
            if(maxMatchIdFromApi == 0) {
                int maxMatchIdFromDb = footballMatchDao.getMaxId();
                url += maxMatchIdFromDb + 1;
            } else {
                url += maxMatchIdFromApi +1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer)resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxMatchIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> matchList = (List<Map<String,Object>>)resultObj.get("results");
                    if(matchList != null && matchList.size() >0) {
                        int size = matchList.size();
                        for(int i =0;i<size;i++) {
                            FootballMatch footballMatch = insertOrUpdateMatch(matchList,i);
                            footballMatchDao.insert(footballMatch);
                            // put footballMatch into redis
                            redisService.set(FootballMatchKey.matchKey,String.valueOf(footballMatch.getId()),footballMatch);
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
            String url = getTimeUrl(namiConfig.getFootballMatchUrl());
            if(maxMatchTimeFromApi == 0) {
                int maxUpdateAt = footballMatchDao.getMaxUpdatedAt();
                url += maxUpdateAt + 1;
            } else {
                url += maxMatchTimeFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer)resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxMatchTimeFromApi = (Integer) query.get("max_time");
                if(total >0) {
                    List<Map<String,Object>> matchList = (List<Map<String,Object>>)resultObj.get("results");
                    if(matchList != null && matchList.size() >0) {
                        int size = matchList.size();
                        for(int i =0;i<size;i++) {
                            FootballMatch footballMatch = insertOrUpdateMatch(matchList,i);
                            footballMatchDao.updateDataById(footballMatch);
                            // delete redis data
                            redisService.delete(FootballMatchKey.matchKey,String.valueOf(footballMatch.getId()));
                            redisService.set(FootballMatchKey.matchKey,String.valueOf(footballMatch.getId()),footballMatch);
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



    /**
     * Get All competitions
     * */
    //@Scheduled(cron = "0 */10 * * * ?")
    public void getAllCompetitionOrUpdate(){
        /* First time get maxId from data, and then get maxId from api*/
        while (true) {
            String url = getIdUrl(namiConfig.getFootballCompetitionUrl());
            if(maxCompetitionIdFromApi == 0){
                int maxIdFromDb = footballCompetitionDao.getMaxId();
                url += maxIdFromDb+1;
            } else {
                url += maxCompetitionIdFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer)resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxCompetitionIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> competitionList = (List<Map<String,Object>>)resultObj.get("results");
                    if (competitionList != null && competitionList.size() > 0) {
                        int size = competitionList.size();
                        for(int i = 0;i < size; i++) {
                            FootballCompetition footballCompetition = insertOrUpdateFootballCompetition(competitionList, i);
                            footballCompetitionDao.insert(footballCompetition);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("get all competitions is wrong, please try again!");
            }
        }

        // Update competition

       while(true) {
            String url = getTimeUrl(namiConfig.getFootballCompetitionUrl());
            if(maxCompetitionTimeFromApi == 0) {
                Integer maxUpdateAtFromDb = footballCompetitionDao.getMaxUpdatedAt();
                url += maxUpdateAtFromDb+1;
            } else {
                url += maxCompetitionTimeFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer)resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxCompetitionTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> updateCompetitionList = (List<Map<String, Object>>) resultObj.get("results");
                    if (updateCompetitionList != null && updateCompetitionList.size() >0) {
                        int size = updateCompetitionList.size();
                        for(int i = 0;i < size; i++) {
                            FootballCompetition footballCompetition = insertOrUpdateFootballCompetition(updateCompetitionList, i);
                            footballCompetitionDao.updateDataById(footballCompetition);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("update competition error, please connect to nami platform");
            }
        }
    }


    /**
     * get all footballTeams
     * */
    //@Scheduled(cron = "0 */10 * * * ?")
    public void getAllTeamOrUpdate(){

        // Get all teams
       while(true) {
           String url;
           url = getIdUrl(namiConfig.getFootballTeamUrl());
           if(maxTeamIdFromApi == 0) {
               int maxTeamId = footballTeamDao.getMaxId();
               url += maxTeamId+1;
           } else {
               url += maxTeamIdFromApi + 1;
           }
           String result = HttpUtil.getNaMiData(url);
           Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
           Integer code = (Integer) resultObj.get("code");
           if (code == 0) {
               Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
               Integer total = (Integer)query.get("total");
               maxTeamIdFromApi = (Integer) query.get("max_id");
               if(total > 0) {
                   List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
                   if(teamsList != null && teamsList.size() > 0) {
                       int size = teamsList.size();
                       for(int i =0;i<size;i++) {
                           FootballTeam footballTeam = getFootballTeam(teamsList, i);
                           footballTeamDao.insert(footballTeam);
                       }
                   }
               } else {
                   break;
               }
           } else {
               log.error("Get all football teams are wrong from name, please connect to nami platform!");
           }
       }
       // Update teams
        while (true) {
            String url = getTimeUrl(namiConfig.getFootballTeamUrl());
            if(maxTeamTimeFromApi == 0){
                Integer maxUpdateAt = footballTeamDao.getMaxUpdatedAt();
                url += maxUpdateAt+1;
            } else {
                url += maxTeamTimeFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxTeamTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
                    if(teamsList != null && teamsList.size() > 0) {
                        int size = teamsList.size();
                        for(int i =0;i<size;i++) {
                            FootballTeam footballTeam = getFootballTeam(teamsList, i);
                            footballTeamDao.updateDataById(footballTeam);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("update all football teams are wrong from nami, please connect to nami platform!");
            }
        }
    }


    /* execute this task every 20 seconds*/
    //@Scheduled(cron = "*/20 * * * * ?")
    public void getUpdateDataFromNami(){
        String url = getNormalUrl(namiConfig.getUpdateDataUrl());
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
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
    //@Scheduled(cron = "*/2 * * * * ?")
    public void getMatchResult(){
        String url = getNormalUrl(namiConfig.getFootballMatchLiveUrl());
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
        if(code == 0)
        {
            List<Map<String,Object>> results = (List<Map<String,Object>>)resultObj.get("results");
            if(results != null && results.size() >0)
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
                       if(score != null && score.size() >0)
                       {
                           matchStatus = (Integer)score.get(1);
                           List<Integer> homeScores = (List<Integer>) score.get(2);
                           if(homeScores != null && homeScores.size() >0) {
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
                           if(awayScores != null && awayScores.size() >0) {
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
                       if(statsList != null && statsList.size() >0)
                       {
                          int statSize = statsList.size();
                          for(int j=0;j<statSize;i++)
                          {
                              Map<String,Object> mp = statsList.get(i);
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

                       FootballMatch footballMatch = redisService.get(FootballMatchKey.matchKey,String.valueOf(matchId),FootballMatch.class);
                       if(footballMatch == null) {
                           footballMatch = footballMatchDao.getFootballMatchById(matchId);
                       }
                       if(matchStatus != 0) {
                           footballMatch.setStatus(matchStatus);
                       }
                       if(homeScore != 0) {
                           footballMatch.setHomeTeamScore(homeScore);
                       }
                       if(awayScore !=0) {
                           footballMatch.setAwayTeamScore(awayScore);
                       }
                       // update redis cache
                        redisService.set(FootballMatchKey.matchKey,String.valueOf(matchId),footballMatch);
                       // update table football_match
                       footballMatchDao.updateDataById(footballMatch);

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

                       // delete redis data and reset data later
                       redisService.delete(FootballMatchKey.matchLiveKey,String.valueOf(matchId));
                       footballMatchLiveDataDao.updateFootballMatchLiveData(footballMatchLiveData);
                   }
                   // put footballMatchLiveData into redis
                   redisService.set(FootballMatchKey.matchLiveKey,String.valueOf(matchId),footballMatchLiveData);
               }
            }
        }




    }









    private void setMatchLineUp(Integer matchId){
        String url = getNormalUrl(namiConfig.getFootballLineUpUrl());
        url += "&id="+matchId;
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        Integer code = (Integer) resultObj.get("code");
        if(code == 0) {
            Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
            Integer confirmed = (Integer)results.get("confirmed");
            // 正式阵容，1-是、0-不是
            if(confirmed == 1) {
                //String homeInfo = (String)results.get("home_formation");
                //String awayInfo = (String)results.get("away_formation");
                List<Map<String,Object>> home = (List<Map<String, Object>>) results.get("home");
                List<Map<String,Object>> away = (List<Map<String, Object>>) results.get("away");
                if(home != null && home.size() >0) {
                    home.stream().forEach(ml ->{
                        HomeMatchLineUp homeMatchLineUp = getFootballHomeMatchLineUp(ml,matchId);
                        HomeMatchLineUp homeMatchLineUpFromDb = homeMatchLineUpDao.getHomeMatchLineUp(matchId);
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
                        AwayMatchLineUp awayMatchLineUpFromDb = awayMatchLineUpDao.getAwayMatchLineUp(matchId);
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





    private static FootballTeam getFootballTeam(List<Map<String, Object>> teamsList, int i) {
        FootballTeam footballTeam = new FootballTeam();
        Map<String,Object> teamMap = teamsList.get(i);
        Integer id = (Integer)teamMap.get("id");
        Integer competitionId = (Integer) teamMap.get("competitionId");
        String nameZh = (String)teamMap.get("name_zh");
        String nameEn = (String)teamMap.get("name_en");
        String logo = (String)teamMap.get("logo");
        Long updatedAt = Long.valueOf((Integer) teamMap.get("updated_at"));
        footballTeam.setId(id);
        footballTeam.setCompetitionId(competitionId);
        footballTeam.setNameZh(nameZh);
        footballTeam.setNameEn(nameEn);
        footballTeam.setLogo(logo);
        footballTeam.setUpdatedAt(updatedAt);
        return footballTeam;
    }


    private static FootballCompetition insertOrUpdateFootballCompetition(List<Map<String, Object>> competitionList, int i) {
        FootballCompetition footballCompetition = new FootballCompetition();
        Map<String,Object> competition = competitionList.get(i);
        Integer competitionId = (Integer)competition.get("id");
        String nameZh = (String)competition.get("name_zh");
        String nameEn = (String)competition.get("name_en");
        String shortNameZh = (String)competition.get("short_name_zh");
        String shortNameEn = (String)competition.get("short_name_en");
        String logo = (String)competition.get("logo");
        Integer type = (Integer) competition.get("type");
        Long updatedAt =  Long.valueOf((Integer)competition.get("updated_at"));

        footballCompetition.setId(competitionId);
        footballCompetition.setNameZh(nameZh);
        footballCompetition.setNameEn(nameEn);
        footballCompetition.setShortNameZh(shortNameZh);
        footballCompetition.setShortNameEn(shortNameEn);
        footballCompetition.setLogo(logo);
        footballCompetition.setType(type);
        footballCompetition.setUpdatedAt(updatedAt);
        return footballCompetition;
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
        footballMatch.setStatus(statusId);
        footballMatch.setHomeTeamScore(homeScore);
        footballMatch.setAwayTeamScore(awayScore);
        footballMatch.setLineUp(lineUp);
        footballMatch.setMatchTime(matchTime);
        footballMatch.setUpdatedAt(updatedTime);
        FootballTeam homeTeam = footballTeamDao.getTeam(footballMatch.getHomeTeamId());
        if(homeTeam != null) {
            footballMatch.setHomeTeamName(homeTeam.getNameZh());
        }
        FootballTeam awayTeam = footballTeamDao.getTeam(footballMatch.getAwayTeamId());
        if(awayTeam != null) {
            footballMatch.setAwayTeamName(awayTeam.getNameZh());
        }
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


}
