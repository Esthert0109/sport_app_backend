package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.dao.FeiJingBasketballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballMatch;
import com.maindark.livestream.domain.feijing.FeiJingBasketballTeam;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@EnableScheduling
public class FeiJingBasketballCurrentMatchTask {
    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingBasketballTeamDao feiJingBasketballTeamDao;

    @Resource
    FeiJingBasketballMatchDao feiJingBasketballMatchDao;

    @Scheduled(cron = "0 0 * * * *")
    public void getCurrentMatch(){
        String url = feiJingConfig.getBasketballTodayMatch();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match->{
                Integer matchId = (Integer) match.get("matchId");
                int existed = feiJingBasketballMatchDao.queryExisted(matchId);
                if(existed <=0) {
                    FeiJingBasketballMatch feiJingBasketballMatch = getFeiJingBasketbllMatch(match);
                    feiJingBasketballMatchDao.insertData(feiJingBasketballMatch);
                }
            });
        }

    }


    public FeiJingBasketballMatch getFeiJingBasketbllMatch(Map<String,Object> match){
        FeiJingBasketballMatch feiJingBasketballMatch = new FeiJingBasketballMatch();
        Integer matchId = (Integer) match.get("matchId");
        Integer kind = (Integer) match.get("kind");
        Integer leagueId = (Integer) match.get("leagueId");
        String leagueCn = (String) match.get("leagueChs");
        String match_date = (String) match.get("matchTime");
        String matchTime = (String) match.get("startTime");
        String homeTeamNameEn = (String) match.get("homeTeamEn");
        String homeTeamNameCn = (String) match.get("homeTeamChs");
        String awayTeamNameEn = (String) match.get("awayTeamEn");
        String awayTeamNameCn = (String) match.get("awayTeamChs");
        Integer homeTeamId = (Integer) match.get("homeTeamId");
        Integer awayTeamId = (Integer) match.get("awayTeamId");
        Integer statusId = (Integer) match.get("matchState");
        String homeTeamScore = (String) match.get("homeScore");
        Integer home1 = (Integer) match.get("home1");
        Integer home2 = (Integer) match.get("home2");
        Integer home3 = (Integer) match.get("home3");
        Integer home4 = (Integer) match.get("home4");
        String awayTeamScore = (String) match.get("awayScore");
        Integer away1 = (Integer) match.get("away1");
        Integer away2 = (Integer) match.get("away2");
        Integer away3 = (Integer) match.get("away3");
        Integer away4 = (Integer) match.get("away4");
        String season = (String) match.get("season");
        Boolean hasStats = (Boolean) match.get("hasStats");
        feiJingBasketballMatch.setHasState(hasStats);
        feiJingBasketballMatch.setMatchId(matchId);
        feiJingBasketballMatch.setCompetitionId(leagueId);
        feiJingBasketballMatch.setCompetitionName(leagueCn);
        feiJingBasketballMatch.setSeason(season);
        feiJingBasketballMatch.setHomeTeamId(homeTeamId);
        feiJingBasketballMatch.setAwayTeamId(awayTeamId);
        feiJingBasketballMatch.setHomeTeamName(homeTeamNameEn);
        feiJingBasketballMatch.setHomeTeamCns(homeTeamNameCn);
        feiJingBasketballMatch.setAwayTeamName(awayTeamNameEn);
        feiJingBasketballMatch.setAwayTeamCns(awayTeamNameCn);
        feiJingBasketballMatch.setHomeScore(StringUtils.equals("",homeTeamScore)?0:Integer.parseInt(homeTeamScore));
        feiJingBasketballMatch.setHFQuarter(home1);
        feiJingBasketballMatch.setHTQuarter(home2);
        feiJingBasketballMatch.setHTQuarter(home3);
        feiJingBasketballMatch.setH4Quarter(home4);
        feiJingBasketballMatch.setAwayScore(StringUtils.equals("",homeTeamScore)?0:Integer.parseInt(awayTeamScore));
        feiJingBasketballMatch.setAFQuarter(away1);
        feiJingBasketballMatch.setASQuarter(away2);
        feiJingBasketballMatch.setATQuarter(away3);
        feiJingBasketballMatch.setA4Quarter(away4);
        feiJingBasketballMatch.setMatchDate(DateUtil.convertStrToNormalDate(match_date));
        feiJingBasketballMatch.setMatchTime(DateUtil.convertStrToNormalTime(match_date));
        feiJingBasketballMatch.setStatus(String.valueOf(statusId));
        FeiJingBasketballTeam homeTeam = feiJingBasketballTeamDao.getTeamLogoByTeamId(homeTeamId);
        if(homeTeam != null) {
            feiJingBasketballMatch.setHomeTeamLogo(homeTeam.getLogo());
            feiJingBasketballMatch.setHomeCoach(homeTeam.getCoachCn());
        }

        FeiJingBasketballTeam awayTeam = feiJingBasketballTeamDao.getTeamLogoByTeamId(awayTeamId);
        if(awayTeam != null) {
            feiJingBasketballMatch.setAwayCoach(awayTeam.getCoachCn());
            feiJingBasketballMatch.setAwayTeamLogo(awayTeam.getLogo());
        }
        return feiJingBasketballMatch;
    }
}
