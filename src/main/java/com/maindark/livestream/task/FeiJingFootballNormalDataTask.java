package com.maindark.livestream.task;

import com.alibaba.fastjson.JSON;
import com.maindark.livestream.dao.FeiJingFootballInfoDao;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.dao.FeiJingFootballTeamDao;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeiJingFootballTeam;
import com.maindark.livestream.domain.feijing.FeiJingInfo;
import com.maindark.livestream.feiJing.FeiJingConfig;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import io.swagger.v3.core.util.Json;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@EnableScheduling
public class FeiJingFootballNormalDataTask {

    @Resource
    FeiJingConfig feiJingConfig;
    @Resource
    FeiJingFootballTeamDao feiJingFootballTeamDao;

    @Resource
    FeiJingFootballMatchDao feiJingFootballMatchDao;

    @Resource
    FeiJingFootballInfoDao feiJingFootballInfoDao;

    @Scheduled(cron = "0 0 0 * * ?")
    public void getFootballMatch(){
        LocalDate localDate = LocalDate.now();
        String from = DateUtil.convertLocalDateToStr(localDate);
        String url = feiJingConfig.getTeamMatch()+from;
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> matchList = (List<Map<String,Object>>) resultObj.get("matchList");
        if(matchList != null && !matchList.isEmpty()) {
            matchList.forEach(match ->{
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
                FeiJingFootballTeam  homeTeam = feiJingFootballTeamDao.getTeamLogoByTeamId(homeTeamId);
                if(homeTeam != null) {
                    feiJingFootballMatch.setHomeTeamLogo(homeTeam.getLogo());
                    feiJingFootballMatch.setHomeCoach(homeTeam.getCoachCn());
                }

                FeiJingFootballTeam awayTeam = feiJingFootballTeamDao.getTeamLogoByTeamId(awayTeamId);
                if(awayTeam != null) {
                    feiJingFootballMatch.setAwayCoach(awayTeam.getCoachCn());
                    feiJingFootballMatch.setAwayTeamLogo(awayTeam.getLogo());
                }
                int existed = feiJingFootballMatchDao.queryExisted(matchId);
                if(existed <=0) {
                    feiJingFootballMatchDao.insertData(feiJingFootballMatch);
                }
            });
        }

    }

    @Scheduled(cron = "0 0 23 * * ?")
    public void getFeiJingFootballTeam(){
        String url = feiJingConfig.getTeamUrl();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> teamlist = (List<Map<String,Object>>) resultObj.get("teamList");
        if(teamlist != null && !teamlist.isEmpty()) {
            teamlist.forEach(obj ->{
                Integer teamId = (Integer) obj.get("teamId");
                Integer leagueId = (Integer) obj.get("leagueId");
                String nameEn = (String)obj.get("nameEn");
                String nameCn = (String)obj.get("nameChs");
                String logo = (String)obj.get("logo");
                String coachEn = (String)obj.get("coachEn");
                String coachCn = (String)obj.get("coachCn");
                String coachId = (String)obj.get("coachId");
                FeiJingFootballTeam feiJingFootballTeam = new FeiJingFootballTeam();
                feiJingFootballTeam.setTeamId(teamId);
                feiJingFootballTeam.setLogo(logo);
                feiJingFootballTeam.setLeagueId(leagueId);
                feiJingFootballTeam.setNameEn(nameEn);
                feiJingFootballTeam.setNameCn(nameCn);
                feiJingFootballTeam.setCoachEn(coachEn);
                feiJingFootballTeam.setCoachCn(coachCn);
                feiJingFootballTeam.setCoachId(coachId);
                int existed = feiJingFootballTeamDao.queryExisted(teamId);
                if(existed <=0){
                    feiJingFootballTeamDao.insertData(feiJingFootballTeam);
                }
            });
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void getFootballInfo(){
        String url = feiJingConfig.getFootballInfo();
        String result = HttpUtil.sendGet(url);
        Map<String, Object> resultObj = JSON.parseObject(result, Map.class);
        List<Map<String, Object>> infoList = (List<Map<String, Object>>) resultObj.get("list");
        if(infoList != null && !infoList.isEmpty()) {
            infoList.forEach(info -> {
                Integer recordId = (Integer) info.get("recordId");
                int existed = feiJingFootballInfoDao.queryExisted(recordId);

//                if not existed
                if(existed <=0){
                    FeiJingInfo feiJingInfo = getFeiJingFootballInfo(info);
                    feiJingFootballInfoDao.insertData(feiJingInfo);
                }
            });
        }
    }


    public FeiJingInfo getFeiJingFootballInfo(Map<String, Object> info){
        FeiJingInfo feiJingInfo = new FeiJingInfo();
        Integer recordId = (Integer) info.get("recordId");
        Integer matchId = (Integer) info.get("matchId");
        Integer leagueId = (Integer) info.get("leagueId");
        String leagueName = (String) info.get("leagueName");
        String homeTeam = (String) info.get("homeTeam");
        String awayTeam = (String) info.get("awayTeam");
        Integer type = (Integer) info.get("type");
        String title = (String) info.get("title");
        String content = (String) info.get("content");
        String updateTime = (String) info.get("updateTime");

        feiJingInfo.setRecordId(recordId);
        feiJingInfo.setMatchId(matchId);
        feiJingInfo.setLeagueId(leagueId);
        feiJingInfo.setLeagueName(leagueName);
        feiJingInfo.setHomeTeam(homeTeam);
        feiJingInfo.setAwayTeam(awayTeam);
        feiJingInfo.setType(type);
        feiJingInfo.setTitle(title);
        feiJingInfo.setContent(content);
        feiJingInfo.setUpdateTime(updateTime);

        return feiJingInfo;
    }
}
