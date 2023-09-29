package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.BasicDao;
import com.maindark.livestream.dao.FootballCompetitionDao;
import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.FootballTeamDao;
import com.maindark.livestream.domain.FootballCompetition;
import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.domain.FootballTeam;
import com.maindark.livestream.nami.NamiConfig;
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
    FootballCompetitionDao footballCompetitionDao;

    @Resource
    FootballTeamDao footballTeamDao;

    @Resource
    FootballMatchDao footballMatchDao;



    public String getIdUrl(String idUrl){
       String url =  namiConfig.getHost() + idUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&id=";
        return url;
    }

    public String getTimeUrl(String timeUrl){
        String url = namiConfig.getHost() + timeUrl + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&time=";
        return url;
    }

    /* execute every ten minus every day*/
    @Scheduled(cron = "0 */10 * * * ?")
    //second, minute, hour, day of month, month, day(s) of week
    public void getAllMatchOrUpdate(){

        //Get all match
        insertFootballData(namiConfig.getFootballMatchUrl(),maxMatchIdFromApi,footballMatchDao);
        /*while(true) {
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
                        }
                    }
                } else {
                    break;
                }
            }

        }*/

        // update match
        updateFootballData(namiConfig.getFootballMatchUrl(),maxMatchTimeFromApi,footballMatchDao);
        /*while(true) {
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
                        }
                    }
                } else {
                    break;
                }
            }

        }*/
    }



    /**
     * Get All competitions
     * */
    @Scheduled(cron = "0 */10 * * * ?")
    public void getAllCompetitionOrUpdate(){
        /* First time get maxId from data, and then get maxId from api*/
        insertFootballData(namiConfig.getFootballCompetitionUrl(),maxCompetitionIdFromApi,footballCompetitionDao);
        /*while (true) {
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
        }*/

        // Update competition
        updateFootballData(namiConfig.getFootballCompetitionUrl(),maxCompetitionTimeFromApi,footballCompetitionDao);
       /* while(true) {
            String url = getTimeUrl(namiConfig.getFootballCompetitionUrl());
            if(maxCompetitionTimeFromApi == 0) {
                Integer maxUpdateAtFromDb = footballCompetitionDao.getMaxUpdateAt();
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
        }*/
    }


    /**
     * get all footballTeams
     * */
    @Scheduled(cron = "0 */10 * * * ?")
    public void getAllTeamOrUpdate(){
        insertFootballData(namiConfig.getFootballTeamUrl(),maxTeamIdFromApi,footballTeamDao);
        // Get all teams
       /*while(true) {
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
                   if(teamsList != null) {
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
       }*/
       // Update teams
        updateFootballData(namiConfig.getFootballTeamUrl(),maxTeamTimeFromApi,footballTeamDao);
    }

    /**
     * update all footballTeams
     * */
    @Scheduled(cron = "0 */10 * * * ?")
    public void updateFootTeam(){
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
        List<Integer> scores = (List<Integer>)matchMap.get("agg_score");
        Map<String,Object> coverage = (Map<String,Object>)matchMap.get("coverage");
        Integer lineUp = 0;
        if(coverage != null && coverage.size() >0) {
            lineUp = (Integer)coverage.get("lineup");
        }
        Integer homeScore = 0;
        Integer awayScore = 0;
        if(scores != null && scores.size() >0) {
            homeScore = scores.get(0);
            awayScore = scores.get(1);
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
        return footballMatch;
    }

    private void insertFootballData(String apiUrl, Integer maxIdFromApi, BasicDao baseDao){
        while(true) {
            String url = getIdUrl(apiUrl);
            if(maxIdFromApi == 0) {
                int maxTeamId = baseDao.getMaxId();
                url += maxTeamId+1;
            } else {
                url += maxIdFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
                    if(teamsList != null) {
                        int size = teamsList.size();
                        for(int i =0;i<size;i++) {
                            FootballTeam footballTeam = getFootballTeam(teamsList, i);
                            baseDao.insert(footballTeam);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("Get all football teams are wrong from name, please connect to nami platform!");
            }
        }

    }



    private void updateFootballData(String apiUrl, Integer maxTimeFromApi, BasicDao baseDao) {
        while (true) {
            String url = getTimeUrl(apiUrl);
            if(maxTimeFromApi == 0){
                Integer maxUpdateAt = baseDao.getMaxUpdatedAt();
                url += maxUpdateAt+1;
            } else {
                url += maxTimeFromApi+1;
            }

            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
                    if(teamsList != null) {
                        int size = teamsList.size();
                        for(int i =0;i<size;i++) {
                            FootballTeam footballTeam = getFootballTeam(teamsList, i);
                            baseDao.updateDataById(footballTeam);
                        }
                    }
                } else {
                    break;
                }
            } else {
                log.error("Get all football teams are wrong from name, please connect to nami platform!");
            }
        }
    }


}
