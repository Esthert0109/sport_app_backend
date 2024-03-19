package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingBasketballPendingMatchDao;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import com.maindark.livestream.feiJing.FeiJingBasketballConfig;
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
public class FeiJingBasketballLatestMatchesTask {

    @Resource
    FeiJingBasketballPendingMatchDao feijingBasketballPendingMatchDao;

    @Resource
    FeiJingBasketballConfig feiJingBasketballConfig;


    //Per Hours
    @Scheduled(cron = "0 0 */1 * * *")
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
                String homeScore = (String) match.get("homeScore");
                String awayScore = (String) match.get("awayScore");
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


                int existed = feijingBasketballPendingMatchDao.queryExisted(matchId);
                if (existed <= 0) {
                    feijingBasketballPendingMatchDao.insertData(feijingBasketballMatch);
                }
            });
        }
    }
}