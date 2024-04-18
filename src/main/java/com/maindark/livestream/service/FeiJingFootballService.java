package com.maindark.livestream.service;

import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.feijing.FeiJingFootballLineUp;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.redis.FootballListKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.FootballMatchStatus;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.*;
import jakarta.annotation.Resource;
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
public class FeiJingFootballService {
    @Resource
    FeiJingFootballMatchLiveDataDao feiJingFootballMatchLiveDataDao;


    @Resource
    FeiJingFootballMatchDao feiJingFootballMatchDao;


    @Resource
    RedisService redisService;


    @Resource
    FeiJingFootballLineUpDao feiJingFootballLineUpDao;


    @Resource
    FollowService followService;

    public List<FootballMatchVo> getFootBallMatchList(String search, Pageable pageable, Long userId) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<FootballMatchVo> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        list = feiJingFootballMatchDao.getFeiJingFootMatchByCompetitionName(search,from,to,pageSize,offset);
        List<FootballMatchVo> homeTeams = feiJingFootballMatchDao.getFeiJingFootMatchByHomeTeamName(search,from,to,pageSize,offset);
        if(homeTeams != null && !homeTeams.isEmpty()) {
            list.addAll(homeTeams);
        }
        List<FootballMatchVo> awayTeams = feiJingFootballMatchDao.getFeiJingFootMatchByAwayTeamName(search,from,to,pageSize,offset);
        if(awayTeams != null && !awayTeams.isEmpty()) {
            list.addAll(awayTeams);
        }

        if(list != null && !list.isEmpty()) {
            Stream<FootballMatchVo> stream = list.stream().filter(data -> data.getMatchDate().equals(from));
            if(userId != null) {
                stream = stream.peek(footballMatchVo -> {
                    Integer matchId = footballMatchVo.getId();
                    Integer statusId = footballMatchVo.getStatusId();
                    footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                    footballMatchVo.setHasCollected(hasCollected);
                });
            } else {
                stream = stream.peek(footballMatchVo -> {
                    Integer statusId = footballMatchVo.getStatusId();
                    footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                });
            }
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }




    public Map<String,List<FootballMatchVo>> getFootballMatchesStarts(Long userId, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        List<FootballMatchVo> startMatches = feiJingFootballMatchDao.getFeiJingStart(nowDate,tomorrowDate,pageSize,offset);
        if(userId != null) {
            Stream<FootballMatchVo> stream =startMatches.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                footballMatchVo.setHasCollected(hasCollected);
            });
            startMatches = StreamToListUtil.getArrayListFromStream(stream);
        } else {
            Stream<FootballMatchVo> stream =startMatches.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
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
        List<FootballMatchVo> pastMatches = feiJingFootballMatchDao.getFeiJingPast(pastDate,nowDate,pageSize,offset);
        if(userId != null) {
            Stream<FootballMatchVo> stream = pastMatches.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                footballMatchVo.setHasCollected(hasCollected);
            });
            pastMatches = StreamToListUtil.getArrayListFromStream(stream);
        } else {
            Stream<FootballMatchVo> stream = pastMatches.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
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
        List<FootballMatchVo> futureMatches = feiJingFootballMatchDao.getFeiJingFuture(tomorrowDate,futureDate,pageSize,offset);
        if(userId != null) {
            Stream<FootballMatchVo> stream = futureMatches.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                footballMatchVo.setHasCollected(hasCollected);
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(stream);
        } else {
            Stream<FootballMatchVo> stream = futureMatches.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
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
            list = feiJingFootballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
            //return allSportsFootballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
        } else if(StringUtils.equals("false",checkData)){
            list = feiJingFootballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
            //return allSportsFootballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
        } else {
            list = feiJingFootballMatchDao.getFeiJingByDate(date,pageSize,offset);
        }
        if(userId != null) {
            Stream<FootballMatchVo> stream = list.stream().peek(footballMatchVo -> {
                Integer matchId = footballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                footballMatchVo.setHasCollected(hasCollected);
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
            });
            list = StreamToListUtil.getArrayListFromStream(stream);
        } else {
            Stream<FootballMatchVo> stream = list.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
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
            List<FootballMatchVo> pastMatches = feiJingFootballMatchDao.getFeiJingPast(pastDate,nowDate,pageSize,offset);
            List<FootballMatchVo> startMatches = feiJingFootballMatchDao.getFeiJingStart(nowDate,tomorrowDate,pageSize,offset);
            List<FootballMatchVo> futureMatches = feiJingFootballMatchDao.getFeiJingFuture(tomorrowDate,futureDate,pageSize,offset);
            Stream<FootballMatchVo> streamPast = pastMatches.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                if(userId != null) {
                    Integer matchId = footballMatchVo.getId();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                    footballMatchVo.setHasCollected(hasCollected);
                }
            });
            pastMatches = StreamToListUtil.getArrayListFromStream(streamPast);
            Stream<FootballMatchVo> streamStart = startMatches.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                if(userId != null) {
                    Integer matchId = footballMatchVo.getId();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                    footballMatchVo.setHasCollected(hasCollected);
                }
            });
            startMatches = StreamToListUtil.getArrayListFromStream(streamStart);
            Stream<FootballMatchVo> stream = futureMatches.stream().peek(footballMatchVo -> {
                Integer statusId = footballMatchVo.getStatusId();
                footballMatchVo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(statusId));
                if(userId != null) {
                    Integer matchId = footballMatchVo.getId();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                    footballMatchVo.setHasCollected(hasCollected);
                }
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(stream);
        List<FootballMatchVo> list = new ArrayList<>();
        list.addAll(pastMatches);
        list.addAll(startMatches);
        list.addAll(futureMatches);
        results.put("matchList",list);
        return results;
    }



    public FeiJingFootballMatchLineUpVo getFootballMatchLineUpByMatchId(String matchId) {
        FeiJingFootballMatchLineUpVo footballMatchLineUpVo = new FeiJingFootballMatchLineUpVo();
        List<FeiJingFootballLineUp> homeMatchLineUpList = feiJingFootballLineUpDao.getHomeMatchLineUpByMatchId(Integer.parseInt(matchId));
        List<FeiJingFootballLineUp> awayMatchLineUpList = feiJingFootballLineUpDao.getAwayMatchLineUpByMatchId(Integer.parseInt(matchId));
        footballMatchLineUpVo.setHomeMatchLineUpList(homeMatchLineUpList);
        footballMatchLineUpVo.setAwayMatchLineList(awayMatchLineUpList);
        return footballMatchLineUpVo;
    }

    public FeiJingFootballMatchLiveDataVo getMatchLiveData(Integer matchId) {
        FeiJingFootballMatchLiveDataVo feiJingFootballMatchLiveDataVo =  feiJingFootballMatchLiveDataDao.getMatchLiveData(matchId);
        if(feiJingFootballMatchLiveDataVo != null){
            String homePossessionRate = feiJingFootballMatchLiveDataVo.getHomePossessionRate().replace("%","");
            feiJingFootballMatchLiveDataVo.setHomePossessionRate(homePossessionRate);
            String awayPossessionRate = feiJingFootballMatchLiveDataVo.getAwayPossessionRate().replace("%","");
            feiJingFootballMatchLiveDataVo.setAwayPossessionRate(awayPossessionRate);
            String homeFormation = feiJingFootballMatchLiveDataVo.getHomeFormation();
            String awayFormation = feiJingFootballMatchLiveDataVo.getAwayFormation();
            if(homeFormation != null && !homeFormation.isEmpty()){
                char[] attr = homeFormation.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (char c : attr) {
                    sb.append(c);
                    sb.append("-");
                }
                String s = sb.substring(0,sb.lastIndexOf("-"));
                feiJingFootballMatchLiveDataVo.setHomeFormation(s);
            }
            if(awayFormation != null && !awayFormation.isEmpty()){
                char[] attr = awayFormation.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (char c : attr) {
                    sb.append(c);
                    sb.append("-");
                }
                String s = sb.substring(0,sb.lastIndexOf("-"));
                feiJingFootballMatchLiveDataVo.setAwayFormation(s);
            }
        }
        return feiJingFootballMatchLiveDataVo;
    }

}
