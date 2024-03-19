package com.maindark.livestream.feiJing;


import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballPendingMatchDao;
import com.maindark.livestream.domain.feijing.FeiJingBasketballTeam;
import com.maindark.livestream.domain.feijing.FeijingBasketballPendingMatch;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class FeiJingApiBasketballPendingService {

    @Resource
    FeiJingBasketballConfig feiJingBasketballConfig;

    @Resource
    FeiJingBasketballPendingMatchDao feijingBasketballPendingMatchDao;


    public void getAllMatches() {
        String url = feiJingBasketballConfig.getMatch();
        String result = HttpUtil.sendGet(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        List<Map<String, Object>> matchList = (List<Map<String, Object>>) resultObj.get("matchList");
        if (matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match ->{

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
                String homeScore = (String) match.get("homeScore");
                String awayScore = (String) match.get("awayScore");
                String season = (String) match.get("season");
                String kind = (String) match.get("matchKind");
                String updatedDate = (String) match.get("updateTime");

                FeijingBasketballPendingMatch feijingBasketballPendingMatch = new FeijingBasketballPendingMatch();

                feijingBasketballPendingMatch.setMatchId(matchId);
                feijingBasketballPendingMatch.setCompetitionId(competitionId);
                feijingBasketballPendingMatch.setLeagueEn(leagueEn);
                feijingBasketballPendingMatch.setLeagueChs(leagueChs);
                feijingBasketballPendingMatch.setMatchTime(matchTime);
                feijingBasketballPendingMatch.setMatchState(matchState);
                feijingBasketballPendingMatch.setHomeTeamId(homeTeamId);
                feijingBasketballPendingMatch.setHomeTeamEn(homeTeamEn);
                feijingBasketballPendingMatch.setHomeTeamChs(homeTeamChs);
                feijingBasketballPendingMatch.setAwayTeamId(awayTeamId);
                feijingBasketballPendingMatch.setAwayTeamEn(awayTeamEn);
                feijingBasketballPendingMatch.setAwayTeamChs(awayTeamChs);
                feijingBasketballPendingMatch.setHomeScore(homeScore);
                feijingBasketballPendingMatch.setAwayScore(awayScore);
                feijingBasketballPendingMatch.setSeason(season);
                feijingBasketballPendingMatch.setKind(kind);
                feijingBasketballPendingMatch.setUpdatedDate(updatedDate);

                //Get Logo from others domain with team id
                FeiJingBasketballTeam homeTeam = feijingBasketballPendingMatchDao.getTeamLogo(homeTeamId);
                if(homeTeam != null) {
                    feijingBasketballPendingMatch.setHomeTeamLogo(homeTeam.getLogo());
                }

                //Parameter came from FeiJingBasketballTeam
                    FeiJingBasketballTeam awayTeam = feijingBasketballPendingMatchDao.getTeamLogo(awayTeamId);
                if(awayTeam != null) {
                    feijingBasketballPendingMatch.setAwayTeamLogo(awayTeam.getLogo());
                }


                int existed = feijingBasketballPendingMatchDao.queryExisted(matchId);
                if (existed <= 0) {
                    feijingBasketballPendingMatchDao.insertData(feijingBasketballPendingMatch);
                }
            });
        }
    }
}
