package com.maindark.livestream.service;

import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.BasketballLineUp;
import com.maindark.livestream.domain.BasketballMatch;
import com.maindark.livestream.domain.BasketballTeam;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.BasketballMatchStatus;
import com.maindark.livestream.util.DateUtil;
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
public class FeiJingBasketballService {
    @Resource
    FeiJingBasketballMatchDao feiJingBasketballMatchDao;

    @Resource
    FeiJingBasketballLineUpDao feiJingBasketballLineUpDao;

    @Resource
    FeiJingBasketballMatchLiveDataDao feiJingBasketballMatchLiveDataDao;

    @Resource
    BasketballTeamDao basketballTeamDao;

    @Resource
    BasketballMatchDao basketballMatchDao;

    @Resource
    FootballLiveAddressDao footballLiveAddressDao;

    @Resource
    FollowService followService;

    public List<BasketballMatchVo> getBasketBallMatchList(String search, Pageable pageable, Long userId) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();

        List<BasketballMatchVo> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        list = feiJingBasketballMatchDao.getFeiJingBasketMatchByCompetitionName(search,from,to,pageSize,offset);
        List<BasketballMatchVo> homeTeams = feiJingBasketballMatchDao.getFeiJingBasketMatchByHomeTeamName(search,from,to,pageSize,offset);
        if(homeTeams != null && !homeTeams.isEmpty()) {
            list.addAll(homeTeams);
        }
        List<BasketballMatchVo> awayTeams = feiJingBasketballMatchDao.getFeiJingBasketMatchByAwayTeamName(search,from,to,pageSize,offset);
        if(awayTeams != null && !awayTeams.isEmpty()) {
            list.addAll(awayTeams);
        }

        if(list != null && !list.isEmpty()) {
            Stream<BasketballMatchVo> stream = list.stream().filter(data -> data.getMatchDate().equals(from));
            stream= stream.peek(basketballMatchVo -> {
                basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatus()));
                if(userId != null){
                    int matchId = basketballMatchVo.getId();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                    basketballMatchVo.setHasCollected(hasCollected);
                }
            });
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }


    public Map<String, List<BasketballMatchVo>> getBasketballMatchesStarts(Long userId, Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        List<BasketballMatchVo> startMatches = feiJingBasketballMatchDao.getFeiJingStart(nowDate,tomorrowDate,pageSize,offset);
        Stream<BasketballMatchVo> stream = startMatches.stream().peek(basketballMatchVo -> {
            basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatus()));
            if(userId != null) {
                int matchId = basketballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                basketballMatchVo.setHasCollected(hasCollected);
            }
        });
        startMatches = StreamToListUtil.getArrayListFromStream(stream);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("start",startMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesPasts(Long userId,Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all past matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate past = now.minusDays(6);
        String pastDate = DateUtil.convertDateToStr(past);
        List<BasketballMatchVo> pastMatches = feiJingBasketballMatchDao.getFeiJingPast(pastDate,nowDate,pageSize,offset);
        Stream<BasketballMatchVo> stream = pastMatches.stream().peek(basketballMatchVo -> {
            basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatus()));
            if(userId != null) {
                int matchId = basketballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                basketballMatchVo.setHasCollected(hasCollected);
            }
        });
        pastMatches = StreamToListUtil.getArrayListFromStream(stream);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("pass",pastMatches);
        return results;
    }


    public List<BasketballMatchVo> getMatchListByDate(Long userId,String date, Pageable pageable, String checkData) {
        date = DateUtil.convertDateToStr(DateUtil.convertStringToDate(date));
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<BasketballMatchVo> list = null;
        if(StringUtils.equals("true",checkData)){
            list = feiJingBasketballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
//            return allSportsBasketballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
        } else if(StringUtils.equals("false",checkData)){
            list = feiJingBasketballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
//            return allSportsBasketballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
        } else {
            list = feiJingBasketballMatchDao.getFeiJingMatchByDate(date,pageSize,offset);
        }
        Stream<BasketballMatchVo> stream = list.stream().peek(basketballMatchVo -> {
            basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatus()));
            if(userId != null) {
                int matchId = basketballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                basketballMatchVo.setHasCollected(hasCollected);
            }
        });
        list = StreamToListUtil.getArrayListFromStream(stream);
        return list;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesFuture(Long userId,Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all future matches in seven days
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        LocalDate future = now.plusDays(6);
        String futureDate = DateUtil.convertDateToStr(future);
        List<BasketballMatchVo> futureMatches = feiJingBasketballMatchDao.getFeiJingFuture(tomorrowDate,futureDate,pageSize,offset);
        Stream<BasketballMatchVo> stream = futureMatches.stream().peek(basketballMatchVo -> {
            basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatus()));
            if(userId != null) {
                int matchId = basketballMatchVo.getId();
                Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), matchId);
                basketballMatchVo.setHasCollected(hasCollected);
            }

        });
        futureMatches = StreamToListUtil.getArrayListFromStream(stream);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("future",futureMatches);
        return results;
    }



    public BasketballMatchLineUpVo getBasketballMatchLineUpByMatchId(Integer matchId) {
        BasketballMatchLineUpVo basketballMatchLineUpVo = new BasketballMatchLineUpVo();
        List<BasketballLineUp> home = feiJingBasketballLineUpDao.getLineUpByMatchId(matchId, LineUpType.HOME.getType());
        List<BasketballLineUp> away = feiJingBasketballLineUpDao.getLineUpByMatchId(matchId, LineUpType.AWAY.getType());
        basketballMatchLineUpVo.setHome(home);
        basketballMatchLineUpVo.setAway(away);
        return basketballMatchLineUpVo;
    }

    public FeiJingBasketballLiveDataVo getMatchLiveData(Integer matchId) {
        return feiJingBasketballMatchLiveDataDao.getMatchLiveDataByMatchId(matchId);
    }

    public String getLiveAddress(String homeTeamName, String awayTeamName) {
        homeTeamName = homeTeamName.trim();
        BasketballTeam homTeam = basketballTeamDao.getTeamByName(homeTeamName);
        if(homTeam == null){
            throw new GlobalException(CodeMsg.BASKETBALL_TEAM_IS_NOT_EXISTED);
        }
        awayTeamName = awayTeamName.trim();
        BasketballTeam awayTeam = basketballTeamDao.getTeamByName(awayTeamName);
        if(awayTeam == null) {
            throw new GlobalException(CodeMsg.BASKETBALL_TEAM_IS_NOT_EXISTED);
        }

        // get home team id
        Long homeTeamId = homTeam.getTeamId();
        // get away team id
        Long awayTeamId = awayTeam.getTeamId();
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        BasketballMatch basketballMatch = basketballMatchDao.getBasketMatchByHomeTeamIdAndAwayTeamId(homeTeamId,awayTeamId,nowSeconds);
        if(basketballMatch == null){
            throw new GlobalException(CodeMsg.BASKETBALL_MATCH_IS_NOT_EXISTED);
        }
        FootballLiveAddressVo footballLiveAddressVo = footballLiveAddressDao.getLiveAddressByMatchId(basketballMatch.getMatchId().intValue(),2);
        if(footballLiveAddressVo == null){
            throw new GlobalException(CodeMsg.BASKETBALL_LIVE_ADDRESS_IS_NOT_EXISTED);
        }
        return StringUtils.equals("",footballLiveAddressVo.getPushUrl3()) ? footballLiveAddressVo.getPushUrl1():footballLiveAddressVo.getPushUrl3();
    }


}
