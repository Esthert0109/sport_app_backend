package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.BasketballCompetitionDao;
import com.maindark.livestream.dao.BasketballMatchDao;
import com.maindark.livestream.dao.BasketballTeamDao;
import com.maindark.livestream.domain.BasketballCompetition;
import com.maindark.livestream.domain.BasketballMatch;
import com.maindark.livestream.domain.BasketballTeam;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NamiBasketballDataService {

    @Resource
    NamiConfig namiConfig;

    @Resource
    BasketballCompetitionDao basketballCompetitionDao;
    @Resource
    BasketballMatchDao basketballMatchDao;

    @Resource
    BasketballTeamDao basketballTeamDao;

    public Map<String,Object> getBasketBallDataByDate(String date) {
        String url = namiConfig.getNormalUrl(namiConfig.getBasketballDataUrl()) + "&date=" + date;
        String result = HttpUtil.getNaMiData(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        Integer code = (Integer) resultObj.get("code");
        if (code == null) {
            log.error("get nami basketball live data error:{}", resultObj.get("err"));
            throw new GlobalException(CodeMsg.FOOT_BALL_ERROR);
        }
        if (code == 0) {
            Map<String,Object> results = (Map<String,Object>)resultObj.get("results");
            List<Map<String,Object>> matchList = (List<Map<String,Object>>)results.get("match");
            List<Map<String,Object>> competitionList = (List<Map<String,Object>>)results.get("competition");
            List<Map<String,Object>> teamList = (List<Map<String,Object>>)results.get("team");

            competitionList.stream().forEach(ml ->{
                BasketballCompetition basketballCompetition = insertOrUpdateBasketballCompetition(ml);
                int exist = basketballCompetitionDao.queryExist(basketballCompetition.getCompetitionId());
                if(exist <=0){
                    basketballCompetitionDao.insertData(basketballCompetition);
                } else {
                    basketballCompetitionDao.updateData(basketballCompetition);
                }
            });
            teamList.stream().forEach(ml ->{
                BasketballTeam basketballTeam = insertOrUpdateTeam(ml);
                int exist = basketballTeamDao.queryExist(basketballTeam.getTeamId());
                if(exist <=0) {
                    basketballTeamDao.insertData(basketballTeam);
                } else {
                    basketballTeamDao.updateData(basketballTeam);
                }
            });
            matchList.stream().forEach(ml ->{
                BasketballMatch basketballMatch = insertOrUpdateMatch(ml);
                int exist = basketballMatchDao.queryExist(basketballMatch.getMatchId());
                if(exist <= 0) {
                    basketballMatchDao.insertData(basketballMatch);
                } else {
                    basketballMatchDao.updateData(basketballMatch);
                }
            });
        }

        return null;
    }

    private BasketballTeam insertOrUpdateTeam(Map<String, Object> teamMap) {
        BasketballTeam basketballTeam = new BasketballTeam();
        Number teamId = (Number) teamMap.get("id");
        String nameZh = (String)teamMap.get("name");
        String logo = (String)teamMap.get("logo");
        basketballTeam.setTeamId(teamId.longValue());
        basketballTeam.setCompetitionId(1l);
        basketballTeam.setConferenceId(3);
        basketballTeam.setNameZh(nameZh);
        basketballTeam.setLogo(logo);
        return basketballTeam;
    }

    private BasketballCompetition insertOrUpdateBasketballCompetition(Map<String, Object> ml) {
        BasketballCompetition basketballCompetition = new BasketballCompetition();
        Number competitionId = (Number)ml.get("id");
        String nameZh = (String)ml.get("name");
        String logo = (String)ml.get("logo");

        basketballCompetition.setCompetitionId(competitionId.longValue());
        basketballCompetition.setNameZh(nameZh);
        basketballCompetition.setLogo(logo);
        basketballCompetition.setType(1);
        return basketballCompetition;
    }

    private BasketballMatch insertOrUpdateMatch(Map<String, Object> matchMap) {
        BasketballMatch basketballMatch = new BasketballMatch();
        Number matchId = ((Number)matchMap.get("id"));
        Integer seasonId = (Integer)matchMap.get("season_id");
        Number competitionId = (Number)matchMap.get("competition_id");
        Number homeTeamId = (Number)matchMap.get("home_team_id");
        Number awayTeamId = (Number)matchMap.get("away_team_id");
        Integer statusId = (Integer)matchMap.get("status_id");
        Long matchTime = Long.valueOf((Integer) matchMap.get("match_time"));
        Integer periodCount = (Integer)matchMap.get("period_count");
        Integer kind = (Integer)matchMap.get("kind");
        Integer homeScore = 0;
        Integer awayScore = 0;

        //get home_scores from api
        List<Integer> homeScores = (List<Integer>)matchMap.get("home_scores");
        List<Integer> awayScores = (List<Integer>)matchMap.get("away_scores");
        if(homeScores != null && homeScores.size() >0) {
            Integer score0 = homeScores.get(0);
            Integer score1 = homeScores.get(1);
            Integer score2 = homeScores.get(2);
            Integer score3 = homeScores.get(3);
            Integer score4 = homeScores.get(4);
            homeScore = score0 + score1 + score2+ score3+ score4;
        }

        if(awayScores != null && awayScores.size() >0) {
            Integer score0 = awayScores.get(0);
            Integer score1 = awayScores.get(1);
            Integer score2 = awayScores.get(2);
            Integer score3 = awayScores.get(3);
            Integer score4 = awayScores.get(4);
            awayScore = score0 + score1 + score2+ score3 +score4;
        }
        if(homeTeamId != null){
            BasketballTeam homeTeam = basketballTeamDao.getTeamById(homeTeamId.longValue());
            if(homeTeam != null) {
                basketballMatch.setHomeTeamName(homeTeam.getNameZh());
                basketballMatch.setHomeTeamLogo(homeTeam.getLogo());
            }

        }
        if(awayTeamId != null) {
            BasketballTeam awayTeam = basketballTeamDao.getTeamById(awayTeamId.longValue());
            if(awayTeam != null) {
                basketballMatch.setAwayTeamName(awayTeam.getNameZh());
                basketballMatch.setAwayTeamLogo(awayTeam.getLogo());
            }

        }
        Long updatedTime = Long.valueOf((Integer)matchMap.get("updated_at"));
        basketballMatch.setMatchId(matchId.longValue());
        basketballMatch.setSeasonId(seasonId);
        basketballMatch.setCompetitionId(competitionId.longValue());
        basketballMatch.setHomeTeamId(homeTeamId.longValue());
        basketballMatch.setAwayTeamId(awayTeamId.longValue());
        basketballMatch.setPeriodCount(periodCount);
        basketballMatch.setStatusId(statusId);
        basketballMatch.setKind(kind);
        basketballMatch.setHomeScore(homeScore);
        basketballMatch.setAwayScore(awayScore);
        basketballMatch.setMatchTime(matchTime);
        basketballMatch.setUpdatedAt(updatedTime);
        return basketballMatch;
    }

}
