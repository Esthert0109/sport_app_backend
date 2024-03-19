package com.maindark.livestream.feiJing;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maindark.livestream.dao.FeiJingFootballLineUpDao;
import com.maindark.livestream.dao.FeiJingFootballMatchDao;
import com.maindark.livestream.dao.FeiJingFootballTeamDao;
import com.maindark.livestream.domain.AllSportsFootballLineUp;
import com.maindark.livestream.domain.feijing.FeiJingFootballLineUp;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeiJingFootballTeam;
import com.maindark.livestream.enums.IsFirst;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.enums.TeamEnum;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeiHingApiFootballService {

    @Resource
    FeiJingConfig feiJingConfig;
    @Resource
    FeiJingFootballTeamDao feiJingFootballTeamDao;

    @Resource
    FeiJingFootballMatchDao feiJingFootballMatchDao;

    @Resource
    FeiJingFootballLineUpDao feiJingFootballLineUpDao;


    public void getAllTeams(){
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


    public List<Map<String,Object>> getMatchByDate(String matchDate) {
        String url = feiJingConfig.getTeamMatch() + matchDate;
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
        return matchList;

    }

    public List<Map<String, Object>> getLineUpData() {
        String url = feiJingConfig.getLineup();
        String result = HttpUtil.sendGet(url);
        Map<String,Object> resultObj = JSON.parseObject(result,Map.class);
        List<Map<String,Object>> lineupList = (List<Map<String,Object>>) resultObj.get("lineupList");
        if(lineupList != null && !lineupList.isEmpty()){
            lineupList.forEach(match ->{
                Integer matchId = (Integer)match.get("matchId");
                String homeFormation = (String)match.get("homeArray");
                String awayFormation = (String)match.get("awayArray");
                feiJingFootballMatchDao.updateFormationByMatchId(homeFormation,awayFormation,matchId);
                JSONArray homeLineup = (JSONArray) match.get("homeLineup");
                JSONArray awayLineup = (JSONArray) match.get("awayLineup");
                JSONArray homeBackup = (JSONArray) match.get("homeBackup");
                JSONArray awayBackup = (JSONArray) match.get("awayBackup");
                getMatchLineUp(homeLineup,matchId,homeBackup, TeamEnum.HOME.getCode());
                getMatchLineUp(awayLineup,matchId,awayBackup, TeamEnum.AWAY.getCode());
            });
        }
        return lineupList;
    }

    private void getMatchLineUp(JSONArray startingLineups, Integer matchId,JSONArray substitutes,String teamType){
        setAllSportsFootballMatchLineUp(startingLineups, matchId, IsFirst.YES.getCode(),teamType);
        setAllSportsFootballMatchLineUp(substitutes, matchId,IsFirst.NO.getCode(), teamType);
    }

    private void setAllSportsFootballMatchLineUp(JSONArray jsonArray, Integer matchId, Integer first, String teamType) {
        if(jsonArray != null){
            for (Object o : jsonArray) {
                Map<String, Object> map = (Map<String, Object>) o;
                String playerName = (String) map.get("nameChs");
                Integer playerNumber = (Integer) map.get("number");
                String playerPosition = (String) map.get("positionId");
                Integer playerId = (Integer) map.get("playerId");
                FeiJingFootballLineUp footballLineUp = getAllSportsLineUp(playerId, playerNumber, playerPosition, matchId, playerName, first, teamType);
                int exist = feiJingFootballLineUpDao.queryExists(playerId, matchId);
                if (exist <= 0) {
                    feiJingFootballLineUpDao.insert(footballLineUp);
                }
            }
        }
    }

    private FeiJingFootballLineUp getAllSportsLineUp(Integer playerId, Integer playNumber, String playPosition, Integer matchId, String playerName, Integer first, String teamType) {
        FeiJingFootballLineUp footballLineUp = new FeiJingFootballLineUp();
        if(StringUtils.equals("0",teamType)){
            footballLineUp.setType(LineUpType.HOME.getType());
        } else {
            footballLineUp.setType(LineUpType.AWAY.getType());
        }
        footballLineUp.setPlayerId(playerId);
        footballLineUp.setMatchId(matchId);
        footballLineUp.setShirtNumber(playNumber);
        footballLineUp.setPosition(Integer.parseInt(playPosition));
        footballLineUp.setPlayerName(playerName);
        footballLineUp.setFirst(first);
        return footballLineUp;
    }


}
