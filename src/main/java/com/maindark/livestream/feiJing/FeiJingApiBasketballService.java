package com.maindark.livestream.feiJing;


import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeijingBasketballMatchDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FeiJingApiBasketballService {

    @Resource
    FeiJingBasketballConfig feiJingBasketballConfig;

    @Resource
    FeijingBasketballMatchDao feijingBasketballMatchDao;

//    public List<Map<String,Object>> getMatches() {
//        String url = feiJingBasketballConfig.getTeamMatch();
//        String result = HttpUtil.sendGet(url);
//        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
//        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
//        if(matchList != null && !matchList.isEmpty()) {
//            matchList.forEach(match ->{
//
//                FeijingBasketballMatch feijingBasketballMatch= new FeijingBasketballMatch();
//                Integer matchId = (Integer) match.get("matchId");
//                Integer competitionId = (Integer) match.get("leagueId");
//                String leagueEn = (String) match.get("leagueEn");
//                String leagueChs = (String) match.get("leagueChs");
//                String matchTime = (String) match.get("matchTime");
//                Integer matchState = (Integer) match.get("matchState");
//                Integer homeTeamId = (Integer) match.get("homeTeamId");
//                String homeTeamEn = (String) match.get("homeTeamEn");
//                String homeTeamChs = (String) match.get("homeTeamChs");
//                Integer awayTeamId = (Integer) match.get("awayTeamId");
//                String awayTeamEn = (String) match.get("awayTeamEn");
//                String awayTeamChs = (String) match.get("awayTeamChs");
//                Integer homeScore = (Integer) match.get("homeScore");
//                Integer awayScore = (Integer) match.get("awayScore");
//                String season = (String) match.get("season");
//                String kind = (String) match.get("matchKind");
//                String updatedDate = (String) match.get("updateTime");
//
//                feijingBasketballMatch.setMatchId(matchId);
//                feijingBasketballMatch.setCompetitionId(competitionId);
//                feijingBasketballMatch.setLeagueEn(leagueEn);
//                feijingBasketballMatch.setLeagueChs(leagueChs);
//                feijingBasketballMatch.setMatchTime(matchTime);
//                feijingBasketballMatch.setMatchState(matchState);
//                feijingBasketballMatch.setHomeTeamId(homeTeamId);
//                feijingBasketballMatch.setHomeTeamEn(homeTeamEn);
//                feijingBasketballMatch.setHomeTeamChs(homeTeamChs);
//                feijingBasketballMatch.setAwayTeamId(awayTeamId);
//                feijingBasketballMatch.setAwayTeamEn(awayTeamEn);
//                feijingBasketballMatch.setAwayTeamChs(awayTeamChs);
//                feijingBasketballMatch.setHomeScore(homeScore);
//                feijingBasketballMatch.setAwayScore(awayScore);
//                feijingBasketballMatch.setSeason(season);
//                feijingBasketballMatch.setKind(kind);
//                feijingBasketballMatch.setUpdatedDate(updatedDate);
//
//
//                int existed = feijingBasketballMatchDao.queryExisted(matchId);
//                if(existed <=0) {
//                    feijingBasketballMatchDao.insertData(feijingBasketballMatch);
//                }
//            });
//        }
//        return matchList;
//
//    }
//}

    public void getAllMatches() {
        String url = feiJingBasketballConfig.getMatch();
        String result = HttpUtil.sendGet(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        List<Map<String, Object>> matchList = (List<Map<String, Object>>) resultObj.get("matchList");
        if (matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match -> {


                Integer matchId = (Integer) match.get("matchId");
                Integer competitionId = (Integer) match.get("leagueId");
                String leagueEn = (String) match.get("leagueEn");
                String leagueChs = (String) match.get("leagueChs");
                String matchTime = (String) match.get("matchTime");
                Integer matchState = (Integer) match.get("matchState");
                Integer homeTeamId = (Integer) match.get("homeTeamId");
                String homeTeamEn = (String) match.get("homeTeamEn");
                String homeTeamChs = (String) match.get("homeTeamChs");
                Integer awayTeamId = (Integer) match.get("awayTeamId");
                String awayTeamEn = (String) match.get("awayTeamEn");
                String awayTeamChs = (String) match.get("awayTeamChs");
                Integer homeScore = (Integer) match.get("homeScore");
                Integer awayScore = (Integer) match.get("awayScore");
                String season = (String) match.get("season");
                String kind = (String) match.get("matchKind");
                String updatedDate = (String) match.get("updateTime");

                FeijingBasketballMatch feijingBasketballMatch = new FeijingBasketballMatch();

                feijingBasketballMatch.setMatchId(matchId);
                feijingBasketballMatch.setCompetitionId(competitionId);
                feijingBasketballMatch.setLeagueEn(leagueEn);
                feijingBasketballMatch.setLeagueChs(leagueChs);
                feijingBasketballMatch.setMatchTime(matchTime);
                feijingBasketballMatch.setMatchState(matchState);
                feijingBasketballMatch.setHomeTeamId(homeTeamId);
                feijingBasketballMatch.setHomeTeamEn(homeTeamEn);
                feijingBasketballMatch.setHomeTeamChs(homeTeamChs);
                feijingBasketballMatch.setAwayTeamId(awayTeamId);
                feijingBasketballMatch.setAwayTeamEn(awayTeamEn);
                feijingBasketballMatch.setAwayTeamChs(awayTeamChs);
                feijingBasketballMatch.setHomeScore(homeScore);
                feijingBasketballMatch.setAwayScore(awayScore);
                feijingBasketballMatch.setSeason(season);
                feijingBasketballMatch.setKind(kind);
                feijingBasketballMatch.setUpdatedDate(updatedDate);


                int existed = feijingBasketballMatchDao.queryExisted(matchId);
                if (existed <= 0) {
                    feijingBasketballMatchDao.insertData(feijingBasketballMatch);
                }
            });
        }
    }
}
