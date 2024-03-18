package com.maindark.livestream.feiJing;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.dao.FeiJingBasketballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballMatch;
import com.maindark.livestream.domain.feijing.FeiJingBasketballTeam;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class FeiHingApiBasketballService {

    @Resource
    FeiJingConfig feiJingConfig;



    @Resource
    FeiJingBasketballTeamDao feiJingBasketballTeamDao;

    @Resource
    FeiJingBasketballMatchDao feiJingBasketballMatchDao;

    public void getAllTeams() {
        String url = feiJingConfig.getBasketballTeam();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> teamlist = (List<Map<String,Object>>) resultObj.get("TeamList");
        if(teamlist != null && !teamlist.isEmpty()) {
            teamlist.forEach(obj ->{
                Integer teamId = (Integer) obj.get("teamId");
                Integer leagueId = (Integer) obj.get("leagueId");
                String nameEn = (String)obj.get("nameEn");
                String nameCn = (String)obj.get("nameChs");
                String logo = (String)obj.get("logo");
                String coachEn = (String)obj.get("headCoachEn");
                String coachCn = (String)obj.get("headCoachCn");
                FeiJingBasketballTeam feiJingBasketballTeam = new FeiJingBasketballTeam();
                feiJingBasketballTeam.setTeamId(teamId);
                feiJingBasketballTeam.setLogo(logo);
                feiJingBasketballTeam.setLeagueId(leagueId);
                feiJingBasketballTeam.setNameEn(nameEn);
                feiJingBasketballTeam.setNameCn(nameCn);
                feiJingBasketballTeam.setCoachEn(coachEn);
                feiJingBasketballTeam.setCoachCn(coachCn);
                int existed = feiJingBasketballTeamDao.queryExisted(teamId);
                if(existed <=0){
                    feiJingBasketballTeamDao.insertData(feiJingBasketballTeam);
                }
            });
        }
    }

    public List<Map<String, Object>> getMatchesByDate(String matchDate) {
        String url = feiJingConfig.getBasketballMatch() +matchDate;
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match ->{
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
                String home1 = (String) match.get("home1");
                String home2 = (String) match.get("home2");
                String home3 = (String) match.get("home3");
                String home4 = (String) match.get("home4");
                String awayTeamScore = (String) match.get("awayScore");
                String away1 = (String) match.get("away1");
                String away2 = (String) match.get("away2");
                String away3 = (String) match.get("away3");
                String away4 = (String) match.get("away4");
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
                feiJingBasketballMatch.setHomeScore((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(homeTeamScore)));
                feiJingBasketballMatch.setHFQuarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(home1)));
                feiJingBasketballMatch.setHSQuarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(home2)));
                feiJingBasketballMatch.setHTQuarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(home3)));
                feiJingBasketballMatch.setH4Quarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(home4)));
                feiJingBasketballMatch.setAwayScore((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(awayTeamScore)));
                feiJingBasketballMatch.setAFQuarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(away1)));
                feiJingBasketballMatch.setASQuarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(away2)));
                feiJingBasketballMatch.setATQuarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(away3)));
                feiJingBasketballMatch.setA4Quarter((StringUtils.equals("", homeTeamScore) ? 0 : Integer.parseInt(away4)));
                feiJingBasketballMatch.setMatchDate(DateUtil.convertStrToNormalDate(match_date));
                feiJingBasketballMatch.setMatchTime(DateUtil.convertStrToNormalTime(match_date));
                feiJingBasketballMatch.setStatus(String.valueOf(statusId));
                FeiJingBasketballTeam  homeTeam = feiJingBasketballTeamDao.getTeamLogoByTeamId(homeTeamId);
                if(homeTeam != null) {
                    feiJingBasketballMatch.setHomeTeamLogo(homeTeam.getLogo());
                    feiJingBasketballMatch.setHomeCoach(homeTeam.getCoachCn());
                }

                FeiJingBasketballTeam awayTeam = feiJingBasketballTeamDao.getTeamLogoByTeamId(awayTeamId);
                if(awayTeam != null) {
                    feiJingBasketballMatch.setAwayCoach(awayTeam.getCoachCn());
                    feiJingBasketballMatch.setAwayTeamLogo(awayTeam.getLogo());
                }
                int existed = feiJingBasketballMatchDao.queryExisted(matchId);
                if(existed <=0) {
                    feiJingBasketballMatchDao.insertData(feiJingBasketballMatch);
                }
            });
        }
        return matchList;
    }

}
