package com.maindark.livestream.task;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.*;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.util.HttpUtil;
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
public class FootballNormalDataTask {
    private Integer maxVenueIdFromApi = 0;
    private Integer maxVenueTimeFromApi = 0;
    private Integer maxRefereeIdFromApi = 0;
    private Integer maxRefereeTimeFromApi = 0;

    private Integer maxTeamIdFromApi = 0;
    private Integer maxTeamTimeFromApi = 0;

    private Integer maxCompetitionIdFromApi = 0;

    private Integer maxCompetitionTimeFromApi = 0;

    private Integer maxCoachIdFromApi = 0;
    private Integer maxCoachTimeFromApi = 0;
    @Resource
    NamiConfig namiConfig;

    @Resource
    FootballVenueDao footballVenueDao;

    @Resource
    FootballRefereeDao footballRefereeDao;

    @Resource
    FootballTeamDao footballTeamDao;

    @Resource
    FootballCompetitionDao footballCompetitionDao;

    @Resource
    FootballCoachDao footballCoachDao;


    /**
     * get all football venue
     *
     */

    //@Scheduled(cron = "0 */1 * * * ?")
    public void getAllFootballVenue(){
        while(true) {
            String url;
            url = namiConfig.getIdUrl(namiConfig.getFootballVenueUrl());
            if(maxVenueIdFromApi == 0) {
                Integer maxVenueId = footballVenueDao.getMaxId();
                if (maxVenueId == null){
                    url += maxVenueIdFromApi + 1;
                } else {
                    url += maxVenueId+1;
                }
            } else {
                url += maxVenueIdFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxVenueIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> venueList = (List<Map<String, Object>>) resultObj.get("results");
                    if(venueList != null && !venueList.isEmpty()) {
                        venueList.stream().forEach(ml ->{
                            FootballVenue footballVenue = getFootballVenue(ml);
                            footballVenueDao.insert(footballVenue);
                        });
                    }
                } else {
                    break;
                }
            } else {
                log.error("Get all football venues are wrong from name, please connect to nami platform!");
            }
        }
        // Update teams
        while (true) {
            String url = namiConfig.getTimeUrl(namiConfig.getFootballVenueUrl());
            if(maxVenueTimeFromApi == 0){
                Integer maxUpdateAt = footballVenueDao.getMaxUpdatedAt();
                if(maxUpdateAt == null){
                    url += maxVenueTimeFromApi+1;
                } else {
                    url += maxUpdateAt+1;
                }
            } else {
                url += maxVenueTimeFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxVenueTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> venueList = (List<Map<String, Object>>) resultObj.get("results");
                    if(venueList != null && !venueList.isEmpty()) {
                        venueList.stream().forEach(ml ->{
                            FootballVenue footballVenue = getFootballVenue(ml);
                            footballVenueDao.updateFootballVenue(footballVenue);
                        });
                    }
                } else {
                    break;
                }
            } else {
                log.error("update all football venues are wrong from nami, please connect to nami platform!");
            }
        }
    }



    /**
     * get all referees
     *
     */
    // @Scheduled(cron = "0 */1 * * * ?")
    public void getAllReferees(){
        while(true) {
            String url;
            url = namiConfig.getIdUrl(namiConfig.getFootballRefereeUrl());
            if(maxRefereeIdFromApi == 0) {
                Integer maxRefereeId = footballRefereeDao.getMaxId();
                if(maxRefereeId== null) {
                    url += maxRefereeIdFromApi + 1;
                } else {
                    url += maxRefereeId+1;
                }
            } else {
                url += maxRefereeIdFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxRefereeIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> refereeList = (List<Map<String, Object>>) resultObj.get("results");
                    if(refereeList != null && !refereeList.isEmpty()) {
                        refereeList.stream().forEach(ml ->{
                            FootballReferee footballReferee = getFootballReferee(ml);
                            footballRefereeDao.insert(footballReferee);
                        });
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
            String url = namiConfig.getTimeUrl(namiConfig.getFootballRefereeUrl());
            if(maxRefereeTimeFromApi == 0){
                Integer maxUpdateAt = footballRefereeDao.getMaxUpdatedAt();
                if(maxUpdateAt == null){
                    url += maxRefereeTimeFromApi+1;
                } else {
                    url += maxUpdateAt+1;
                }
            } else {
                url += maxRefereeTimeFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxRefereeTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> refereeList = (List<Map<String, Object>>) resultObj.get("results");
                    if(refereeList != null && !refereeList.isEmpty()) {
                        refereeList.stream().forEach(ml ->{
                            FootballReferee footballReferee = getFootballReferee(ml);
                            footballRefereeDao.updateFootballReferee(footballReferee);
                        });
                    }
                } else {
                    break;
                }
            } else {
                log.error("update all football referees are wrong from nami, please connect to nami platform!");
            }
        }
    }


    /**
     * get all footballTeams
     * */
   // @Scheduled(cron = "0 */1 * * * ?")
    public void getAllTeamOrUpdate(){

        // Get all teams
        while(true) {
            String url;
            url = namiConfig.getIdUrl(namiConfig.getFootballTeamUrl());
            if(maxTeamIdFromApi == 0) {
                Integer maxTeamId = footballTeamDao.getMaxId();
                if(maxTeamId == null) {
                    url += maxTeamIdFromApi + 1;
                } else {
                    url += maxTeamId+1;
                }
            } else {
                url += maxTeamIdFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxTeamIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
                    if(teamsList != null && !teamsList.isEmpty()) {
                        teamsList.stream().forEach(ml ->{
                            FootballTeam footballTeam = getFootballTeam(ml);
                            footballTeamDao.insert(footballTeam);
                        });
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
            String url = namiConfig.getTimeUrl(namiConfig.getFootballTeamUrl());
            if(maxTeamTimeFromApi == 0){
                Integer maxUpdateAt = footballTeamDao.getMaxUpdatedAt();
                if(maxUpdateAt == null) {
                    url += maxTeamTimeFromApi+1;
                } else {
                    url += maxUpdateAt+1;
                }
            } else {
                url += maxTeamTimeFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxTeamTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> teamsList = (List<Map<String, Object>>) resultObj.get("results");
                    if(teamsList != null && !teamsList.isEmpty()) {
                      teamsList.stream().forEach(ml ->{
                          FootballTeam footballTeam = getFootballTeam(ml);
                          footballTeamDao.updateDataById(footballTeam);
                      });
                    }
                } else {
                    break;
                }
            } else {
                log.error("update all football teams are wrong from nami, please connect to nami platform!");
            }
        }
    }

    /**
     * get all coaches
     */
    //@Scheduled(cron = "0 */1 * * * ?")
    public void getAllFootballCoach(){
        while(true) {
            String url;
            url = namiConfig.getIdUrl(namiConfig.getFootballCoachUrl());
            if(maxCoachIdFromApi == 0) {
                Integer maxCoachId = footballCoachDao.getMaxId();
                if(maxCoachId == null) {
                    url += maxCoachIdFromApi + 1;
                } else {
                    url += maxCoachId+1;
                }

            } else {
                url += maxCoachIdFromApi + 1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxCoachIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> coachesList = (List<Map<String, Object>>) resultObj.get("results");
                    if(coachesList != null && !coachesList.isEmpty()) {
                        coachesList.stream().forEach(ml ->{
                            FootballCoach footballCoach = getFootballCoach(ml);
                            footballCoachDao.insert(footballCoach);
                        });
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
            String url = namiConfig.getTimeUrl(namiConfig.getFootballCoachUrl());
            if(maxCoachTimeFromApi == 0){
                Integer maxUpdateAt = footballCoachDao.getMaxUpdatedAt();
                if(maxUpdateAt == null) {
                    url += maxCoachTimeFromApi+1;
                } else {
                    url += maxUpdateAt+1;
                }
            } else {
                url += maxCoachTimeFromApi+1;
            }
            String result = HttpUtil.getNaMiData(url);
            Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
            Integer code = (Integer) resultObj.get("code");
            if (code == null){
                log.error("nami error:{}",resultObj.get("err"));
                break;
            }
            if (code == 0) {
                Map<String,Object> query = (Map<String,Object>)resultObj.get("query");
                Integer total = (Integer)query.get("total");
                maxCoachTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> coachesList = (List<Map<String, Object>>) resultObj.get("results");
                    if(coachesList != null && !coachesList.isEmpty()) {
                       coachesList.stream().forEach(ml ->{
                           FootballCoach footballCoach = getFootballCoach(ml);
                           footballCoachDao.updateDataById(footballCoach);
                       });
                    }
                } else {
                    break;
                }
            } else {
                log.error("update all football teams are wrong from nami, please connect to nami platform!");
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
            String url = namiConfig.getIdUrl(namiConfig.getFootballCompetitionUrl());
            if(maxCompetitionIdFromApi == 0){
                Integer maxIdFromDb = footballCompetitionDao.getMaxId();
                if(maxIdFromDb == null){
                    url += maxCompetitionIdFromApi+1;
                } else {
                    url += maxIdFromDb+1;
                }
            } else {
                url += maxCompetitionIdFromApi+1;
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
                maxCompetitionIdFromApi = (Integer) query.get("max_id");
                if(total > 0) {
                    List<Map<String,Object>> competitionList = (List<Map<String,Object>>)resultObj.get("results");
                    if (competitionList != null && !competitionList.isEmpty()) {
                       competitionList.stream().forEach(ml ->{
                           FootballCompetition footballCompetition = insertOrUpdateFootballCompetition(ml);
                           footballCompetitionDao.insert(footballCompetition);
                       });
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
            String url = namiConfig.getTimeUrl(namiConfig.getFootballCompetitionUrl());
            if(maxCompetitionTimeFromApi == 0) {
                Integer maxUpdateAtFromDb = footballCompetitionDao.getMaxUpdatedAt();
                if (maxUpdateAtFromDb == null) {
                    url += maxCompetitionTimeFromApi+1;
                } else {
                    url += maxUpdateAtFromDb+1;
                }
            } else {
                url += maxCompetitionTimeFromApi+1;
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
                maxCompetitionTimeFromApi = (Integer)query.get("max_time");
                if(total > 0) {
                    List<Map<String,Object>> updateCompetitionList = (List<Map<String, Object>>) resultObj.get("results");
                    if (updateCompetitionList != null && !updateCompetitionList.isEmpty()) {
                       updateCompetitionList.stream().forEach(ml->{
                           FootballCompetition footballCompetition = insertOrUpdateFootballCompetition(ml);
                           footballCompetitionDao.updateDataById(footballCompetition);
                       });
                    }
                } else {
                    break;
                }
            } else {
                log.error("update competition error, please connect to nami platform");
            }
        }
    }






    private static FootballVenue getFootballVenue(Map<String, Object> ml) {
        FootballVenue footballVenue = new FootballVenue();
        Integer id = (Integer)ml.get("id");
        String nameZh = (String)ml.get("name_zh");
        String nameEn = (String)ml.get("name_en");
        Integer capacity = (Integer) ml.get("capacity");
        Long updatedAt = Long.valueOf((Integer) ml.get("updated_at"));
        footballVenue.setId(id);
        footballVenue.setCapacity(capacity);
        footballVenue.setNameZh(nameZh);
        footballVenue.setNameEn(nameEn);
        footballVenue.setUpdatedAt(updatedAt);
        return footballVenue;
    }


    private static FootballReferee getFootballReferee(Map<String, Object> ml) {
        FootballReferee footballReferee = new FootballReferee();
        Integer id = (Integer)ml.get("id");
        String nameZh = (String)ml.get("name_zh");
        String nameEn = (String)ml.get("name_en");
        Integer birthday = (Integer) ml.get("birthday");
        Integer age = (Integer) ml.get("age");
        Long updatedAt = Long.valueOf((Integer) ml.get("updated_at"));
        footballReferee.setId(id);
        footballReferee.setBirthday(birthday);
        footballReferee.setNameZh(nameZh);
        footballReferee.setNameEn(nameEn);
        footballReferee.setAge(age);
        footballReferee.setUpdatedAt(updatedAt);
        return footballReferee;
    }


    private static FootballTeam getFootballTeam(Map<String, Object> teamMap) {
        FootballTeam footballTeam = new FootballTeam();
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

    private static FootballCoach getFootballCoach(Map<String, Object> teamMap) {
        FootballCoach footballCoach = new FootballCoach();
        Integer id = (Integer)teamMap.get("id");
        Integer teamId = (Integer) teamMap.get("team_id");
        String nameZh = (String)teamMap.get("name_zh");
        String nameEn = (String)teamMap.get("name_en");
        String logo = (String)teamMap.get("logo");
        String preferredFormation = (String)teamMap.get("preferred_formation");
        Long updatedAt = Long.valueOf((Integer) teamMap.get("updated_at"));
        footballCoach.setId(id);
        footballCoach.setTeamId(teamId);
        footballCoach.setNameZh(nameZh);
        footballCoach.setNameEn(nameEn);
        footballCoach.setLogo(logo);
        footballCoach.setPreferredFormation(preferredFormation);
        footballCoach.setUpdatedAt(updatedAt);
        return footballCoach;
    }


    private static FootballCompetition insertOrUpdateFootballCompetition(Map<String, Object> competition) {
        FootballCompetition footballCompetition = new FootballCompetition();
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
}
