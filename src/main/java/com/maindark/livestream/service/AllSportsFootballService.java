package com.maindark.livestream.service;

import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.AllSportsFootballLineUp;
import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.FootballListKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class AllSportsFootballService {


    @Resource
    AllSportsHomeMatchLineUpDao allSportsHomeMatchLineUpDao;
    @Resource
    AllSportsAwayMatchLineUpDao allSportsAwayMatchLineUpDao;

    @Resource
    AllSportsFootballLiveDataDao allSportsFootballLiveDataDao;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    @Resource
    RedisService redisService;

    @Resource
    FootballTeamDao footballTeamDao;

    @Resource
    FootballMatchDao footballMatchDao;

    @Resource
    FootballLiveAddressDao footballLiveAddressDao;

    @Resource
    AllSportsFootballLineUpDao allSportsFootballLineUpDao;

    public List<FootballMatchVo> getFootBallMatchList(String competitionName, String teamName, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
            throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
        }
        List<FootballMatchVo> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        if (!StringUtils.isBlank(competitionName)){
            list = allSportsFootballMatchDao.getAllSportsFootMatchByCompetitionName(competitionName,from,to,pageSize,offset);
        } else if (!StringUtils.isBlank(teamName)) {
            List<FootballMatchVo> homeTeams = allSportsFootballMatchDao.getAllSportsFootMatchByHomeTeamName(teamName,from,to,pageSize,offset);
            if(homeTeams != null && !homeTeams.isEmpty()) {
                for (FootballMatchVo vo:
                     homeTeams) {
                    list.add(vo);
                }
            }
            List<FootballMatchVo> awayTeams = allSportsFootballMatchDao.getAllSportsFootMatchByAwayTeamName(teamName,from,to,pageSize,offset);
            if(awayTeams != null && !awayTeams.isEmpty()) {
                for (FootballMatchVo vo:
                     awayTeams) {
                    list.add(vo);
                }
            }
        }
        if(list != null && !list.isEmpty()) {
            Stream<FootballMatchVo> stream = list.stream().filter(data -> data.getMatchDate().equals(from));
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }




    public Map<String,List<FootballMatchVo>> getFootballMatchesStarts(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        List<FootballMatchVo> startMatches = allSportsFootballMatchDao.getAllSportsStart(nowDate,tomorrowDate,pageSize,offset);
        Map<String,List<FootballMatchVo>> results = new HashMap<>();
        results.put("start",startMatches);
        return results;
    }

    public Map<String,List<FootballMatchVo>> getFootballMatchesPasts(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all past matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate past = now.minusDays(6);
        String pastDate = DateUtil.convertDateToStr(past);
        List<FootballMatchVo> pastMatches = allSportsFootballMatchDao.getAllSportsPast(pastDate,nowDate,pageSize,offset);
        Map<String,List<FootballMatchVo>> results = new HashMap<>();
        results.put("pass",pastMatches);
        return results;
    }

    public Map<String,List<FootballMatchVo>> getFootballMatchesFuture(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all future matches in seven days
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        LocalDate future = now.plusDays(6);
        String futureDate = DateUtil.convertDateToStr(future);
        List<FootballMatchVo> futureMatches = allSportsFootballMatchDao.getAllSportsFuture(tomorrowDate,futureDate,pageSize,offset);
        Map<String,List<FootballMatchVo>> results = new HashMap<>();
        results.put("future",futureMatches);
        return results;
    }



    public Map<String, List<FootballMatchVo>> getFootballMatchesInSevenDays(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        Map<String,List<FootballMatchVo>> results = redisService.get(FootballListKey.listKey,nowDate,Map.class);
        if (results == null) {
            LocalDate tomorrow = now.plusDays(1);
            LocalDate future = now.plusDays(6);
            LocalDate past = now.minusDays(6);
            String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
            String futureDate = DateUtil.convertDateToStr(future);
            String pastDate = DateUtil.convertDateToStr(past);
            List<FootballMatchVo> pastMatches = allSportsFootballMatchDao.getAllSportsPast(pastDate,nowDate,pageSize,offset);
            List<FootballMatchVo> startMatches = allSportsFootballMatchDao.getAllSportsStart(nowDate,tomorrowDate,pageSize,offset);
            List<FootballMatchVo> futureMatches = allSportsFootballMatchDao.getAllSportsFuture(tomorrowDate,futureDate,pageSize,offset);
            results = new HashMap<>();
            results.put("pass",pastMatches);
            results.put("start",startMatches);
            results.put("future",futureMatches);
            redisService.set(FootballListKey.listKey,nowDate,results);
        }
        return results;
    }

    public List<FootballMatchVo> getMatchListByDate(String date,Pageable pageable,String checkData) {
        date = DateUtil.convertDateToStr(DateUtil.convertStringToDate(date));
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        if(StringUtils.equals("true",checkData)){
            return allSportsFootballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
        } else if(StringUtils.equals("false",checkData)){
            return allSportsFootballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
        }
        return allSportsFootballMatchDao.getAllSportsByDate(date,pageSize,offset);
    }

    public AllSportsFootballMatchLineUpVo getFootballMatchLineUpByMatchId(String matchId) {
        AllSportsFootballMatchLineUpVo footballMatchLineUpVo = new AllSportsFootballMatchLineUpVo();
        List<AllSportsFootballLineUp> homeMatchLineUpList = allSportsFootballLineUpDao.getHomeMatchLineUpByMatchId(Long.parseLong(matchId));
        List<AllSportsFootballLineUp> awayMatchLineUpList = allSportsFootballLineUpDao.getAwayMatchLineUpByMatchId(Long.parseLong(matchId));
        footballMatchLineUpVo.setHomeMatchLineUpList(homeMatchLineUpList);
        footballMatchLineUpVo.setAwayMatchLineList(awayMatchLineUpList);
        return footballMatchLineUpVo;
    }

    public AllSportsFootballMatchLiveDataVo getMatchLiveData(Long matchId) {
        AllSportsFootballMatchLiveDataVo allSportsFootballMatchLiveDataVo =  allSportsFootballLiveDataDao.getMatchLiveData(matchId);
        if(allSportsFootballMatchLiveDataVo != null){
            String homePossessionRate = allSportsFootballMatchLiveDataVo.getHomePossessionRate().replace("%","");
            allSportsFootballMatchLiveDataVo.setHomePossessionRate(homePossessionRate);
            String awayPossessionRate = allSportsFootballMatchLiveDataVo.getAwayPossessionRate().replace("%","");
            allSportsFootballMatchLiveDataVo.setAwayPossessionRate(awayPossessionRate);
        }
        return allSportsFootballMatchLiveDataVo;
    }

    public String getLiveAddress(String teamName) {
        FootballTeamVo footballTeamVo = footballTeamDao.getTeamBynName(teamName);
        if(footballTeamVo == null){
            throw new GlobalException(CodeMsg.FOOT_TEAM_IS_NOT_EXISTED);
        }
        // get home team id
        Integer teamId = footballTeamVo.getId();
        FootballMatch footballMatch = footballMatchDao.getFootballMatchByHomeTeamId(teamId);
        if(footballMatch == null){
            throw new GlobalException(CodeMsg.FOOTBALL_MATCH_IS_NOT_EXISTED);
        }
        FootballLiveAddressVo footballLiveAddressVo = footballLiveAddressDao.getLiveAddressByMatchId(footballMatch.getId());
        if(footballLiveAddressVo == null){
            throw new GlobalException(CodeMsg.FOOTBALL_LIVE_ADDRESS_IS_NOT_EXISTED);
        }
        return StringUtils.equals("",footballLiveAddressVo.getPushUrl3()) ? footballLiveAddressVo.getPushUrl1():footballLiveAddressVo.getPushUrl3();
    }
}
