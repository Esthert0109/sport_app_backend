package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.dao.FeiJingFootballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeiJingFootballTeam;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

@Slf4j
public class FeiJingFootballCurrentMatchTask {
    @Resource
    FeiJingConfig feiJingConfig;


    @Resource
    FeiJingFootballMatchDao feiJingFootballMatchDao;

    @Resource
    FeiJingFootballTeamDao feiJingFootballTeamDao;

    @Scheduled(cron = "0 0 * * * *")
    public void getCurrentMatch(){
        String url = feiJingConfig.getTeamMatch();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match->{
                Integer matchId = (Integer) match.get("matchId");
                int existed = feiJingFootballMatchDao.queryExisted(matchId);
                if(existed <=0) {
                    FeiJingFootballMatch feiJingFootballMatch = getFeiJingFootballMatch(match);
                    feiJingFootballMatchDao.insertData(feiJingFootballMatch);
                }
            });
        }

    }

    public  FeiJingFootballMatch getFeiJingFootballMatch(Map<String, Object> match) {
        FeiJingFootballMatch feiJingFootballMatch = new FeiJingFootballMatch();
        Integer matchId = (Integer) match.get("matchId");
        Integer kind = (Integer) match.get("kind");
        Integer leagueId = (Integer) match.get("leagueId");
        String leagueEn = (String) match.get("leagueEn");
        String leagueEnShort = (String) match.get("leagueEnShort");
        String leagueCnSShort = (String) match.get("leagueChsShort");
        String match_date = (String) match.get("matchTime");
        String matchTime = (String) match.get("startTime");
        String homeTeamNameEn = (String) match.get("homeEn");
        String homeTeamNameCn = (String) match.get("homeChs");
        String awayTeamNameEn = (String) match.get("awayEn");
        String awayTeamNameCn = (String) match.get("awayChs");
        Integer homeTeamId = (Integer) match.get("homeId");
        Integer awayTeamId = (Integer) match.get("awayId");
        Integer statusId = (Integer) match.get("state");
        Integer homeTeamScore = (Integer) match.get("homeScore");
        Integer awayTeamScore = (Integer) match.get("awayScore");
        String lineUp = (String) match.get("hasLineup");
        String updateTime = (String) match.get("updateTime");
        String season = (String) match.get("season");
        String venue = (String) match.get("locationCn");
        feiJingFootballMatch.setMatchId(matchId);
        feiJingFootballMatch.setCompetitionId(leagueId);
        feiJingFootballMatch.setLeagueEn(leagueEn);
        feiJingFootballMatch.setLeagueEnShort(leagueEnShort);
        feiJingFootballMatch.setLeagueCnsShort(leagueCnSShort);
        feiJingFootballMatch.setSeason(season);
        feiJingFootballMatch.setKind(kind);
        feiJingFootballMatch.setHomeTeamId(homeTeamId);
        feiJingFootballMatch.setAwayTeamId(awayTeamId);
        feiJingFootballMatch.setHomeTeamNameEn(homeTeamNameEn);
        feiJingFootballMatch.setHomeTeamNameCn(homeTeamNameCn);
        feiJingFootballMatch.setAwayTeamNameEn(awayTeamNameEn);
        feiJingFootballMatch.setAwayTeamNameCn(awayTeamNameCn);
        feiJingFootballMatch.setHomeTeamScore(homeTeamScore);
        feiJingFootballMatch.setAwayTeamScore(awayTeamScore);
        feiJingFootballMatch.setMatchDate(DateUtil.convertStrToNormalDate(match_date));
        feiJingFootballMatch.setMatchTime(DateUtil.convertStrToNormalTime(match_date));
        feiJingFootballMatch.setVenue(venue);
        feiJingFootballMatch.setLineUp(StringUtils.equals("",lineUp)?0:1);
        feiJingFootballMatch.setUpdatedTime(updateTime);
        feiJingFootballMatch.setStatusId(statusId);
        feiJingFootballMatch.setMatchTime(matchTime);
        FeiJingFootballTeam homeTeam = feiJingFootballTeamDao.getTeamLogoByTeamId(homeTeamId);
        if(homeTeam != null) {
            feiJingFootballMatch.setHomeTeamLogo(homeTeam.getLogo());
            feiJingFootballMatch.setHomeCoach(homeTeam.getCoachCn());
        }

        FeiJingFootballTeam awayTeam = feiJingFootballTeamDao.getTeamLogoByTeamId(awayTeamId);
        if(awayTeam != null) {
            feiJingFootballMatch.setAwayCoach(awayTeam.getCoachCn());
            feiJingFootballMatch.setAwayTeamLogo(awayTeam.getLogo());
        }
        return feiJingFootballMatch;
    }


}
