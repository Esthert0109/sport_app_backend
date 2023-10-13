package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.allSports.AllSportsConfig;
import com.maindark.livestream.dao.AllSportsFootballCompetitionDao;
import com.maindark.livestream.dao.AllSportsFootballTeamDao;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import com.maindark.livestream.vo.FootballMatchLineUpVo;
import com.maindark.livestream.vo.FootballMatchLiveDataVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class AllSportsApiService {

    @Resource
    AllSportsConfig allSportsConfig;

    @Resource
    AllSportsFootballCompetitionDao allSportsFootballCompetitionDao;
    @Resource
    AllSportsFootballTeamDao allSportsFootballTeamDao;

    public List<FootballMatchVo> getFootBallMatchList(String competitionName, String teamName) {
        if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
            throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
        }
        List<FootballMatchVo> list = null;
        Integer leagueId = null;
        Integer teamId = null;
        String url = "";
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        if (!StringUtils.isBlank(competitionName)){
            leagueId = allSportsFootballCompetitionDao.getAllSportsFootballCompetitionByName(competitionName.trim());
            url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixturesLeagueId()).replace("{}",String.valueOf(leagueId))+"&from="+ from + "&to=" + to;
        } else if (!StringUtils.isBlank(teamName)) {
            teamId = allSportsFootballTeamDao.getAllSportsTeamByName(teamName.trim());
            url = allSportsConfig.getAllSportsApi(allSportsConfig.getFixturesTeamId()).replace("{}",String.valueOf(teamId))+"&from="+ from + "&to=" + to;
        }
        String result = HttpUtil.getNaMiData(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        if(result != null && !resultObj.isEmpty()) {
            List<Map<String,Object>> matchesList = (List<Map<String,Object>>)resultObj.get("result");
            if(matchesList != null && !matchesList.isEmpty()) {
                Stream<FootballMatchVo> footballMatchVoStream = matchesList.stream().map(ml ->{
                    FootballMatchVo footballMatchVo = new FootballMatchVo();
                    footballMatchVo.setId((Integer)ml.get("event_key"));
                    footballMatchVo.setMatchTimeStr((String)ml.get("event_time"));
                    footballMatchVo.setHomeTeamLogo((String)ml.get("home_team_logo"));
                    footballMatchVo.setAwayTeamLogo((String)ml.get("away_team_logo"));
                    footballMatchVo.setHomeTeamName((String)ml.get("event_home_team"));
                    footballMatchVo.setAwayTeamName((String)ml.get("event_away_team"));
                    String matchStatus = (String)ml.get("event_status");
                    if(StringUtils.equals(matchStatus,"")){
                        footballMatchVo.setHomeTeamScore(0);
                        footballMatchVo.setAwayTeamScore(0);
                        footballMatchVo.setStatusStr("");
                    } else {
                        String scores = (String)ml.get("event_final_result");
                        if(StringUtils.equals("",scores)){
                            footballMatchVo.setHomeTeamScore(0);
                            footballMatchVo.setAwayTeamScore(0);
                        } else {
                            String[] scoreArr = scores.split("-");
                            String score1 = scoreArr[0];
                            String score2 = scoreArr[1];
                            footballMatchVo.setHomeTeamScore(Integer.parseInt(scoreArr[0].trim()));
                            footballMatchVo.setAwayTeamScore(Integer.parseInt(scoreArr[1].trim()));
                        }
                        footballMatchVo.setStatusStr((String)ml.get("event_status"));
                    }
                    footballMatchVo.setVenueName((String)ml.get("event_stadium"));
                    footballMatchVo.setRefereeName((String)ml.get("event_referee"));
                    footballMatchVo.setCompetitionName((String)ml.get("league_name"));
                    footballMatchVo.setCompetitionId((Integer)ml.get("league_key"));
                    return footballMatchVo;
                });
                list = getArrayListFromStream(footballMatchVoStream);
            }
        }
        return list;
    }

    public Map<String, List<FootballMatchVo>> getFootballMatchesInSevenDays() {
        return null;
    }

    public List<FootballMatchVo> getMatchListByDate(String date) {
        return null;
    }

    public FootballMatchLineUpVo getFootballMatchLineUpByMatchId(Integer matchId) {
        return null;
    }

    public FootballMatchLiveDataVo getMatchLiveData(Integer matchId) {
        return null;
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
}
