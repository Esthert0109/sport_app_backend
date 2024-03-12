//package com.maindark.livestream.task;
//
//import com.alibaba.fastjson2.JSON;
//import com.maindark.livestream.dao.BasketballCompetitionDao;
//import com.maindark.livestream.dao.BasketballMatchDao;
//import com.maindark.livestream.dao.BasketballTeamDao;
//import com.maindark.livestream.domain.*;
//import com.maindark.livestream.nami.NamiConfig;
//import com.maindark.livestream.util.HttpUtil;
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Slf4j
//@EnableScheduling
//public class BasketballFundamentalTask {
//    private Integer maxCompetitionIdFromApi = 0;
//
//    private Integer maxCompetitionTimeFromApi = 0;
//
//    private Integer maxTeamIdFromApi = 0;
//    private Integer maxTeamTimeFromApi = 0;
//
//    private Integer maxMatchIdFromApi = 0;
//    private Integer maxMatchTimeFromApi = 0;
//
//
//
//    @Resource
//    NamiConfig namiConfig;
//
//    @Resource
//    BasketballCompetitionDao basketballCompetitionDao;
//
//    @Resource
//    BasketballTeamDao basketballTeamDao;
//
//    @Resource
//    BasketballMatchDao basketballMatchDao;
//    @Scheduled(cron = "0 */20 * * * ?")
//    public void getAllBasketballCompetition(){
//        while (true) {
//            String url = namiConfig.getIdUrl(namiConfig.getBasketballCompetitionUrl());
//            if(maxCompetitionIdFromApi == 0){
//                Integer maxIdFromDb = basketballCompetitionDao.getMaxId();
//                if(maxIdFromDb == null){
//                    url += maxCompetitionIdFromApi+1;
//                } else {
//                    url += maxIdFromDb+1;
//                }
//            } else {
//                url += maxCompetitionIdFromApi+1;
//            }
//            String result = HttpUtil.getNaMiData(url);
//            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//            Integer code = (Integer)resultObj.get("code");
//            if (code == null){
//                log.error("nami error:{}",resultObj.get("err"));
//                break;
//            }
//            if (code == 0) {
//                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
//                Integer total = (Integer)query.get("total");
//                maxCompetitionIdFromApi = (Integer) query.get("max_id");
//                if(total > 0) {
//                    List<Map<String,Object>> competitionList = (List<Map<String,Object>>)resultObj.get("results");
//                    if (competitionList != null && !competitionList.isEmpty()) {
//                        competitionList.stream().forEach(ml ->{
//                            BasketballCompetition basketballCompetition = insertOrUpdatebasketballCompetition(ml);
//                            basketballCompetitionDao.insertData(basketballCompetition);
//                        });
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                log.error("get all competitions is wrong, please try again!");
//            }
//        }
//
//        // Update competition
//
//        while(true) {
//            String url = namiConfig.getTimeUrl(namiConfig.getBasketballCompetitionUrl());
//            if(maxCompetitionTimeFromApi == 0) {
//                Integer maxUpdateAtFromDb = basketballCompetitionDao.getMaxUpdatedAt();
//                if (maxUpdateAtFromDb == null) {
//                    url += maxCompetitionTimeFromApi+1;
//                } else {
//                    url += maxUpdateAtFromDb+1;
//                }
//            } else {
//                url += maxCompetitionTimeFromApi+1;
//            }
//            String result = HttpUtil.getNaMiData(url);
//            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//            Integer code = (Integer)resultObj.get("code");
//            if (code == null){
//                log.error("nami error:{}",resultObj.get("err"));
//                break;
//            }
//            if (code == 0) {
//                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
//                Integer total = (Integer)query.get("total");
//                maxCompetitionTimeFromApi = (Integer)query.get("max_time");
//                if(total > 0) {
//                    List<Map<String,Object>> updateCompetitionList = (List<Map<String, Object>>) resultObj.get("results");
//                    if (updateCompetitionList != null && !updateCompetitionList.isEmpty()) {
//                        updateCompetitionList.stream().forEach(ml->{
//                            BasketballCompetition basketballCompetition = insertOrUpdatebasketballCompetition(ml);
//                            basketballCompetitionDao.updateData(basketballCompetition);
//                        });
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                log.error("update competition error, please connect to nami platform");
//            }
//        }
//    }
//
//
//    @Scheduled(cron = "0 */20 * * * ?")
//    public void getAllBasketballTeam(){
//        // Get all teams
//        while(true) {
//            String url;
//            url = namiConfig.getIdUrl(namiConfig.getBasketballTeamUrl());
//            if(maxTeamIdFromApi == 0) {
//                Integer maxTeamId = basketballTeamDao.getMaxId();
//                if(maxTeamId == null) {
//                    url += maxTeamIdFromApi + 1;
//                } else {
//                    url += maxTeamId+1;
//                }
//            } else {
//                url += maxTeamIdFromApi + 1;
//            }
//            String result = HttpUtil.getNaMiData(url);
//            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//            Integer code = (Integer) resultObj.get("code");
//            if (code == null){
//                log.error("nami error:{}",resultObj.get("err"));
//                break;
//            }
//            if (code == 0) {
//                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
//                Integer total = (Integer)query.get("total");
//                maxTeamIdFromApi = (Integer) query.get("max_id");
//                if(total > 0) {
//                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
//                    if(teamsList != null && !teamsList.isEmpty()) {
//                        teamsList.stream().forEach(ml ->{
//                            BasketballTeam basketballTeam = insertOrUpdateBasketballTeam(ml);
//                            basketballTeamDao.insertData(basketballTeam);
//                        });
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                log.error("Get all football teams are wrong from name, please connect to nami platform!");
//            }
//        }
//        // Update teams
//        while (true) {
//            String url = namiConfig.getTimeUrl(namiConfig.getBasketballTeamUrl());
//            if(maxTeamTimeFromApi == 0){
//                Integer maxUpdateAt = basketballTeamDao.getMaxUpdatedAt();
//                if(maxUpdateAt == null) {
//                    url += maxTeamTimeFromApi+1;
//                } else {
//                    url += maxUpdateAt+1;
//                }
//            } else {
//                url += maxTeamTimeFromApi+1;
//            }
//            String result = HttpUtil.getNaMiData(url);
//            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//            Integer code = (Integer) resultObj.get("code");
//            if (code == null){
//                log.error("nami error:{}",resultObj.get("err"));
//                break;
//            }
//            if (code == 0) {
//                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
//                Integer total = (Integer)query.get("total");
//                maxTeamTimeFromApi = (Integer)query.get("max_time");
//                if(total > 0) {
//                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
//                    if(teamsList != null && !teamsList.isEmpty()) {
//                        teamsList.stream().forEach(ml ->{
//                            BasketballTeam basketballTeam = insertOrUpdateBasketballTeam(ml);
//                            basketballTeamDao.updateData(basketballTeam);
//                        });
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                log.error("update all football teams are wrong from nami, please connect to nami platform!");
//            }
//        }
//    }
//
//    @Scheduled(cron = "0 */20 * * * ?")
//    public void getAllBasketballMatch(){
//        while(true) {
//            String url = namiConfig.getIdUrl(namiConfig.getBasketballMatchUrl());
//            if(maxMatchIdFromApi == 0) {
//                Integer maxMatchIdFromDb = basketballMatchDao.getMaxId();
//                if(maxMatchIdFromDb == null) {
//                    url += maxMatchIdFromApi +1;
//                } else {
//                    url += maxMatchIdFromDb + 1;
//                }
//            } else {
//                url += maxMatchIdFromApi +1;
//            }
//            String result = HttpUtil.getNaMiData(url);
//            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//            Integer code = (Integer)resultObj.get("code");
//            if (code == null){
//                log.error("Getting basketballMatch from nami error:{}",resultObj.get("err"));
//                break;
//            }
//            if (code == 0) {
//                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
//                Integer total = (Integer)query.get("total");
//                maxMatchIdFromApi = (Integer) query.get("max_id");
//                if(total > 0) {
//                    List<Map<String,Object>> matchList = (List<Map<String,Object>>)resultObj.get("results");
//                    if(matchList != null && !matchList.isEmpty()) {
//                        matchList.stream().forEach(ml ->{
//                            BasketballMatch basketballMatch = insertOrUpdateMatch(ml);
//                            basketballMatchDao.insertData(basketballMatch);
//                        });
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                log.error("get basketballMatch data from nami are wrong, please connect to the nami platform!");
//            }
//
//        }
//
//        // update match
//        while(true) {
//            String url = namiConfig.getTimeUrl(namiConfig.getFootballMatchUrl());
//            if(maxMatchTimeFromApi == 0) {
//                Integer maxUpdateAt = basketballMatchDao.getMaxUpdatedAt();
//                if(maxUpdateAt == null){
//                    url += maxMatchTimeFromApi + 1;
//                } else {
//                    url += maxUpdateAt + 1;
//                }
//            } else {
//                url += maxMatchTimeFromApi + 1;
//            }
//            String result = HttpUtil.getNaMiData(url);
//            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//            Integer code = (Integer)resultObj.get("code");
//            if (code == null){
//                log.error("Updating basketballMatch from nami error:{}",resultObj.get("err"));
//                break;
//            }
//            if (code == 0) {
//                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
//                Integer total = (Integer)query.get("total");
//                maxMatchTimeFromApi = (Integer) query.get("max_time");
//                if(total >0) {
//                    List<Map<String,Object>> matchList = (List<Map<String,Object>>)resultObj.get("results");
//                    if(matchList != null && !matchList.isEmpty()) {
//                        matchList.stream().forEach(ml ->{
//                            BasketballMatch basketballMatch = insertOrUpdateMatch(ml);
//                            basketballMatchDao.updateData(basketballMatch);
//                        });
//                    }
//                } else {
//                    break;
//                }
//            } else {
//                log.error("update basketballMatch data from nami are wrong, please connect to the nami platform!");
//            }
//
//        }
//    }
//
//
//
//    private BasketballMatch insertOrUpdateMatch(Map<String,Object> matchMap) {
//        BasketballMatch basketballMatch = new BasketballMatch();
//        Number matchId = ((Number)matchMap.get("id"));
//        Integer seasonId = (Integer)matchMap.get("season_id");
//        Number competitionId = (Number)matchMap.get("competition_id");
//        Number homeTeamId = (Number)matchMap.get("home_team_id");
//        Number awayTeamId = (Number)matchMap.get("away_team_id");
//        Integer statusId = (Integer)matchMap.get("status_id");
//        Integer kind = (Integer)matchMap.get("kind");
//        Long matchTime = Long.valueOf((Integer) matchMap.get("match_time"));
//        Integer homeScore = 0;
//        Integer awayScore = 0;
//
//        //get home_scores from api
//        List<Integer> homeScores = (List<Integer>)matchMap.get("home_scores");
//        List<Integer> awayScores = (List<Integer>)matchMap.get("away_scores");
//        if(homeScores != null && homeScores.size() >0) {
//            Integer score0 = homeScores.get(0);
//            Integer score1 = homeScores.get(1);
//            Integer score2 = homeScores.get(2);
//            Integer score3 = homeScores.get(3);
//            Integer score4 = homeScores.get(4);
//            homeScore = score0 + score1 + score2+ score3+ score4;
//        }
//
//        if(awayScores != null && awayScores.size() >0) {
//            Integer score0 = awayScores.get(0);
//            Integer score1 = awayScores.get(1);
//            Integer score2 = awayScores.get(2);
//            Integer score3 = awayScores.get(3);
//            Integer score4 = awayScores.get(4);
//            awayScore = score0 + score1 + score2+ score3 +score4;
//        }
//        if(homeTeamId != null){
//            BasketballTeam homeTeam = basketballTeamDao.getTeamById(homeTeamId.longValue());
//            if(homeTeam != null) {
//                basketballMatch.setHomeTeamName(homeTeam.getNameZh());
//                basketballMatch.setHomeTeamLogo(homeTeam.getLogo());
//            }
//
//        }
//        if(awayTeamId != null) {
//            BasketballTeam awayTeam = basketballTeamDao.getTeamById(awayTeamId.longValue());
//            if(awayTeam != null) {
//                basketballMatch.setAwayTeamName(awayTeam.getNameZh());
//                basketballMatch.setAwayTeamLogo(awayTeam.getLogo());
//            }
//
//        }
//        Long updatedTime = Long.valueOf((Integer)matchMap.get("updated_at"));
//        basketballMatch.setMatchId(matchId.longValue());
//        basketballMatch.setSeasonId(seasonId);
//        basketballMatch.setCompetitionId(competitionId.longValue());
//        basketballMatch.setHomeTeamId(homeTeamId.longValue());
//        basketballMatch.setAwayTeamId(awayTeamId.longValue());
//        basketballMatch.setStatusId(statusId);
//        basketballMatch.setKind(kind);
//        basketballMatch.setHomeScore(homeScore);
//        basketballMatch.setAwayScore(awayScore);
//        basketballMatch.setMatchTime(matchTime);
//        basketballMatch.setUpdatedAt(updatedTime);
//        return basketballMatch;
//    }
//
//
//
//
//
//    private static BasketballCompetition insertOrUpdatebasketballCompetition(Map<String, Object> competition) {
//        BasketballCompetition basketballCompetition = new BasketballCompetition();
//        Number competitionId = (Number)competition.get("id");
//        String nameZh = (String)competition.get("name_zh");
//        String nameEn = (String)competition.get("name_en");
//        String logo = (String)competition.get("logo");
//        Integer type = (Integer) competition.get("type");
//        Long updatedAt =  Long.valueOf((Integer)competition.get("updated_at"));
//        basketballCompetition.setCompetitionId(competitionId.longValue());
//        basketballCompetition.setNameZh(nameZh);
//        basketballCompetition.setNameEn(nameEn);
//        basketballCompetition.setLogo(logo);
//        basketballCompetition.setType(type);
//        basketballCompetition.setUpdatedAt(updatedAt);
//        return basketballCompetition;
//    }
//
//
//    private static BasketballTeam insertOrUpdateBasketballTeam(Map<String, Object> teamMap) {
//        BasketballTeam basketballTeam = new BasketballTeam();
//        Number teamId = (Number) teamMap.get("id");
//        Number competitionId = (Number) teamMap.get("competitionId");
//        String nameZh = (String)teamMap.get("name_zh");
//        String nameEn = (String)teamMap.get("name_en");
//        String logo = (String)teamMap.get("logo");
//        Long updatedAt = Long.valueOf((Integer) teamMap.get("updated_at"));
//        basketballTeam.setTeamId(teamId.longValue());
//        basketballTeam.setCompetitionId(competitionId.longValue());
//        basketballTeam.setNameZh(nameZh);
//        basketballTeam.setNameEn(nameEn);
//        basketballTeam.setLogo(logo);
//        basketballTeam.setUpdatedAt(updatedAt);
//        return basketballTeam;
//    }
//
//
//}
//
//
//
