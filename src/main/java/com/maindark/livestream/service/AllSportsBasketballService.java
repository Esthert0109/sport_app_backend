package com.maindark.livestream.service;

import com.maindark.livestream.dao.*;
import com.maindark.livestream.domain.BasketballLineUp;
import com.maindark.livestream.domain.BasketballMatch;
import com.maindark.livestream.domain.BasketballTeam;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
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
public class AllSportsBasketballService {

    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

    @Resource
    AllSportsBasketballLineUpDao allSportsBasketballLineUpDao;

    @Resource
    AllSportsBasketballMatchLiveDataDao allSportsBasketballMatchLiveDataDao;

    @Resource
    BasketballTeamDao basketballTeamDao;

    @Resource
    BasketballMatchDao basketballMatchDao;

    @Resource
    FootballLiveAddressDao footballLiveAddressDao;

    public List<BasketballMatchVo> getBasketBallMatchList(String competitionName, String teamName, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
            throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
        }
        List<BasketballMatchVo> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String from = DateUtil.convertDateToStr(now);
        String to = DateUtil.convertDateToStr(tomorrow);
        if (!StringUtils.isBlank(competitionName)){
            list = allSportsBasketballMatchDao.getAllSportsBasketMatchByCompetitionName(competitionName,from,to,pageSize,offset);
        } else if (!StringUtils.isBlank(teamName)) {
            List<BasketballMatchVo> homeTeams = allSportsBasketballMatchDao.getAllSportsBasketMatchByHomeTeamName(teamName,from,to,pageSize,offset);
            if(homeTeams != null && !homeTeams.isEmpty()){
                for(BasketballMatchVo vo:homeTeams){
                    list.add(vo);
                }
            }
            List<BasketballMatchVo> awayTeams = allSportsBasketballMatchDao.getAllSportsBasketMatchByAwayTeamName(teamName,from,to,pageSize,offset);
            if(awayTeams != null && !awayTeams.isEmpty()) {
                for (BasketballMatchVo vo:
                     awayTeams) {
                    list.add(vo);
                }
            }
        }
        if(list != null && !list.isEmpty()) {
            Stream<BasketballMatchVo> stream = list.stream().filter(data -> data.getMatchDate().equals(from));
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }


    public Map<String, List<BasketballMatchVo>> getBasketballMatchesStarts(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        List<BasketballMatchVo> startMatches = allSportsBasketballMatchDao.getAllSportsStart(nowDate,tomorrowDate,pageSize,offset);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("start",startMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesPasts(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all past matches
        LocalDate now = LocalDate.now();
        String nowDate = DateUtil.convertDateToStr(now);
        LocalDate past = now.minusDays(6);
        String pastDate = DateUtil.convertDateToStr(past);
        List<BasketballMatchVo> pastMatches = allSportsBasketballMatchDao.getAllSportsPast(pastDate,nowDate,pageSize,offset);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("pass",pastMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesFuture(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer pageSize = pageable.getPageSize();
        // get all future matches in seven days
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        String tomorrowDate = DateUtil.convertDateToStr(tomorrow);
        LocalDate future = now.plusDays(6);
        String futureDate = DateUtil.convertDateToStr(future);
        List<BasketballMatchVo> futureMatches = allSportsBasketballMatchDao.getAllSportsFuture(tomorrowDate,futureDate,pageSize,offset);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("future",futureMatches);
        return results;
    }

    public List<BasketballMatchVo> getMatchListByDate(String date, Pageable pageable, String checkData) {
        date = DateUtil.convertDateToStr(DateUtil.convertStringToDate(date));
        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        if(StringUtils.equals("true",checkData)){
            return allSportsBasketballMatchDao.getTodayNotStartMatches(date,pageSize,offset);
        } else if(StringUtils.equals("false",checkData)){
            return allSportsBasketballMatchDao.getTodayFinishedMatches(date,pageSize,offset);
        }
        return allSportsBasketballMatchDao.getAllSportsByDate(date,pageSize,offset);
    }

    public BasketballMatchLineUpVo getBasketballMatchLineUpByMatchId(long matchId) {
        BasketballMatchLineUpVo basketballMatchLineUpVo = new BasketballMatchLineUpVo();
        List<BasketballLineUp> home = allSportsBasketballLineUpDao.getLineUpByMatchId(matchId, LineUpType.HOME.getType());
        List<BasketballLineUp> away = allSportsBasketballLineUpDao.getLineUpByMatchId(matchId, LineUpType.AWAY.getType());
        basketballMatchLineUpVo.setHome(home);
        basketballMatchLineUpVo.setAway(away);
        return basketballMatchLineUpVo;
    }

    public AllSportsBasketballLiveDataVo getMatchLiveData(Long matchId) {
        return allSportsBasketballMatchLiveDataDao.getMatchLiveDataByMatchId(matchId);
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
