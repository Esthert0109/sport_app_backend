package com.maindark.livestream.service;

import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.AllSportsFootballLineUp;
import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.FootballListKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.FootballMatchStatus;
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

    @Resource
    FollowService followService;

    public List<FootballMatchVo> getFootBallMatchList(String search, Pageable pageable,Long userId) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<FootballMatchVo> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        list = allSportsFootballMatchDao.getAllSportsFootMatchByCompetitionName(search,from,to,pageSize,offset);
        List<FootballMatchVo> homeTeams = allSportsFootballMatchDao.getAllSportsFootMatchByHomeTeamName(search,from,to,pageSize,offset);
        if(homeTeams != null && !homeTeams.isEmpty()) {
            list.addAll(homeTeams);
        }
        List<FootballMatchVo> awayTeams = allSportsFootballMatchDao.getAllSportsFootMatchByAwayTeamName(search,from,to,pageSize,offset);
        if(awayTeams != null && !awayTeams.isEmpty()) {
            list.addAll(awayTeams);
        }

        if(list != null && !list.isEmpty()) {
            Stream<FootballMatchVo> stream = list.stream().filter(data -> data.getMatchDate().equals(from));
            if(userId != null) {
                stream = stream.peek(footballMatchVo -> {
                    Integer matchId = footballMatchVo.getId();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                    footballMatchVo.setHasCollected(hasCollected);
                });
            }
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }




    public Map<String,List<FootballMatchVo>> getFootballMatchesStarts(Long userId,Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        List<FootballMatchVo> startMatches = allSportsFootballMatchDao.getAllSportsStart(nowDate,tomorrowDate,pageSize,offset);
        if(userId != null) {
            Stream<FootballMatchVo> stream =startMatches.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            });
            startMatches = StreamToListUtil.getArrayListFromStream(stream);
        }

        Map<String,List<FootballMatchVo>> results = new HashMap<>();
        results.put("start",startMatches);
        return results;
    }

    public Map<String,List<FootballMatchVo>> getFootballMatchesPasts(Long userId,Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        // get all past matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate past = now.minusDays(6);
        String pastDate = DateUtil.convertDateToStr(past);
        List<FootballMatchVo> pastMatches = allSportsFootballMatchDao.getAllSportsPast(pastDate,nowDate,pageSize,offset);
        if(userId != null) {
            Stream<FootballMatchVo> stream = pastMatches.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            });
            pastMatches = StreamToListUtil.getArrayListFromStream(stream);
        }

        Map<String,List<FootballMatchVo>> results = new HashMap<>();
        results.put("pass",pastMatches);
        return results;
    }

    public Map<String,List<FootballMatchVo>> getFootballMatchesFuture(Long userId,Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        // get all future matches in seven days
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        LocalDate future = now.plusDays(6);
        String futureDate = DateUtil.convertDateToStr(future);
        List<FootballMatchVo> futureMatches = allSportsFootballMatchDao.getAllSportsFuture(tomorrowDate,futureDate,pageSize,offset);
        if(userId != null) {
            Stream<FootballMatchVo> stream = futureMatches.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(stream);
        }

        Map<String,List<FootballMatchVo>> results = new HashMap<>();
        results.put("future",futureMatches);
        return results;
    }

    public List<FootballMatchVo> getMatchListByDate(Long userId,String date,Pageable pageable,String checkData) {
        date = DateUtil.convertDateToStr(DateUtil.convertStringToDate(date));
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<FootballMatchVo> list = null;
        if(StringUtils.equals("true",checkData)){
            list = allSportsFootballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
            //return allSportsFootballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
        } else if(StringUtils.equals("false",checkData)){
            list = allSportsFootballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
            //return allSportsFootballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
        } else {
            list = allSportsFootballMatchDao.getAllSportsByDate(date,pageSize,offset);
        }
        if(userId != null) {
            Stream<FootballMatchVo> stream = list.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            });
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
//        return allSportsFootballMatchDao.getAllSportsByDate(date,pageSize,offset);
        return list;
    }



    public Map<String, List<FootballMatchVo>> getFootballMatchesInSevenDays(Long userId,Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        Map<String,List<FootballMatchVo>> results = new HashMap<>();
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
        Stream<FootballMatchVo> streamPast = pastMatches.stream().peek(footballMatchVo -> {
            if(userId != null) {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            }
        });
        pastMatches = StreamToListUtil.getArrayListFromStream(streamPast);
        Stream<FootballMatchVo> streamStart = startMatches.stream().peek(footballMatchVo -> {
            if(userId != null) {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            }
        });
        startMatches = StreamToListUtil.getArrayListFromStream(streamStart);
        Stream<FootballMatchVo> stream = futureMatches.stream().peek(footballMatchVo -> {
            if(userId != null) {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
            }
        });
        futureMatches = StreamToListUtil.getArrayListFromStream(stream);
            results.put("pass",pastMatches);
            results.put("start",startMatches);
            results.put("future",futureMatches);

        return results;
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

    public String getLiveAddress(String homeTeamName,String awayTeamName) {
        homeTeamName = homeTeamName.trim();
        FootballTeamVo homFootballTeamVo = footballTeamDao.getTeamByName(homeTeamName);
        if(homFootballTeamVo == null){
            throw new GlobalException(CodeMsg.FOOT_TEAM_IS_NOT_EXISTED);
        }
        awayTeamName = awayTeamName.trim();
        FootballTeamVo awayFootballTeamVo = footballTeamDao.getTeamByName(awayTeamName);
        if(awayFootballTeamVo == null) {
            throw new GlobalException(CodeMsg.FOOT_TEAM_IS_NOT_EXISTED);
        }

        // get home team id
        Integer homeTeamId = homFootballTeamVo.getId();
        // get away team id
        Integer awayTeamId = awayFootballTeamVo.getId();
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        FootballMatch footballMatch = footballMatchDao.getFootballMatchByHomeTeamIdAndAwayTeamId(homeTeamId,awayTeamId,nowSeconds);
        if(footballMatch == null){
            throw new GlobalException(CodeMsg.FOOTBALL_MATCH_IS_NOT_EXISTED);
        }
        FootballLiveAddressVo footballLiveAddressVo = footballLiveAddressDao.getLiveAddressByMatchId(footballMatch.getId(),1);
        if(footballLiveAddressVo == null){
            throw new GlobalException(CodeMsg.FOOTBALL_LIVE_ADDRESS_IS_NOT_EXISTED);
        }
        return StringUtils.equals("",footballLiveAddressVo.getPushUrl3()) ? footballLiveAddressVo.getPushUrl1():footballLiveAddressVo.getPushUrl3();
    }
}
