package com.maindark.livestream.feiJing;



import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.dao.FeiJingFootballTeamDao;
import com.maindark.livestream.dao.FeijingBasketballMatchDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeiJingFootballTeam;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeiJingApiBasketballService {

    @Resource
    FeiJingBasketballConfig feiJingBasketballConfig;

    @Resource
    FeijingBasketballMatchDao feijingBasketballMatchDao;

    public void getUpcomingMatches(){

        String url = feiJingBasketballConfig.getTeamMatch();

        String result = HttpUtil.sendGet(url);

        Map<String,Object> resultMap = JSON.parseObject(result,Map.class);

//        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultMap.get("");

        Integer matchId = (Integer) resultMap.get("matchId");
        Integer competitionId = (Integer) resultMap.get("leagueId");
        String leagueEn = (String) resultMap.get("leagueEn");
        String leagueChs = (String) resultMap.get("leagueChs");
        String matchTime = (String) resultMap.get("matchTime");
        Integer matchState = (Integer) resultMap.get("matchState");
        Integer homeTeamId = (Integer) resultMap.get("homeTeamId");
        String homeTeamEn = (String) resultMap.get("homeTeamEn");
        String homeTeamChs = (String) resultMap.get("homeTeamChs");
        Integer awayTeamId = (Integer) resultMap.get("awayTeamId");
        String awayTeamEn = (String) resultMap.get("awayTeamEn");
        String awayTeamChs = (String) resultMap.get("awayTeamChs");
        Integer homeScore = (Integer) resultMap.get("homeScore");
        Integer awayScore = (Integer) resultMap.get("awayScore");
        String season = (String) resultMap.get("season");
        String kind = (String) resultMap.get("matchKind");
        String updatedDate = (String) resultMap.get("updateTime");

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
        feijingBasketballMatch.setAwayTeamId(awayTeamId);
        feijingBasketballMatch.setAwayTeamChs(awayTeamChs);
        feijingBasketballMatch.setHomeScore(homeScore);
        feijingBasketballMatch.setAwayScore(awayScore);
        feijingBasketballMatch.setSeason(season);
        feijingBasketballMatch.setKind(kind);
        feijingBasketballMatch.setUpdatedDate(updatedDate);

        int existed = feijingBasketballMatchDao.queryExisted(matchId);
        if(existed <=0){
            feijingBasketballMatchDao.insertData(feijingBasketballMatch);
        }


    }

}
