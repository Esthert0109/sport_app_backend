package com.maindark.livestream.service;



import com.maindark.livestream.dao.BasketballLineUpDao;
import com.maindark.livestream.dao.BasketballMatchDao;
import com.maindark.livestream.dao.BasketballMatchLiveDataDao;
import com.maindark.livestream.domain.BasketballLineUp;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.BasketballMatchStatus;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.BasketballMatchLineUpVo;
import com.maindark.livestream.vo.BasketballMatchLiveDataVo;
import com.maindark.livestream.vo.BasketballMatchVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class BasketBallService {




    @Resource
    BasketballMatchDao basketballMatchDao;

    @Resource
    BasketballMatchLiveDataDao basketballMatchLiveDataDao;

    @Resource
    BasketballLineUpDao basketballLineUpDao;



    public List<BasketballMatchVo> getBasketballMatchList(String competitionName, String teamName, Pageable pageable) {
        long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        List<BasketballMatchVo> list = null;
        if(StringUtils.isBlank(competitionName) && StringUtils.isBlank(teamName)) {
            throw new GlobalException(CodeMsg.FOOT_BALL_MATCH_PARAMS_ERROR);
        }
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        long nowSeconds = DateUtil.convertDateToLongTime(now);
        long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
        if(!StringUtils.isBlank(competitionName)){
            list = basketballMatchDao.getBasketballMatchByCompetitionName(competitionName,nowSeconds,tomorrowSeconds,limit,offset);
        } else if(!StringUtils.isBlank(teamName)){
            list = basketballMatchDao.getBasketballMatchByTeamName(teamName,nowSeconds,tomorrowSeconds,limit,offset);
        }
        if(list != null && !list.isEmpty()){
            Stream<BasketballMatchVo> stream = list.stream().filter(data ->data.getMatchTime() > nowSeconds).map(data ->{
                Long matchTime = data.getMatchTime() * 1000;
                String timeStr = DateUtil.interceptTime(matchTime);
                data.setMatchTimeStr(timeStr);
                data.setMatchDate(DateUtil.convertLongTimeToMatchDate(matchTime));
                Integer statusId = data.getStatusId();
                data.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(statusId));
                return data;
            });
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }



    public Map<String, List<BasketballMatchVo>> getBasketballMatchesStarts(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        LocalDate tomorrow = now.plusDays(1);
        Long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
        List<BasketballMatchVo> startMatches = basketballMatchDao.getBasketballMatchesStart(nowSeconds,tomorrowSeconds,limit,offset);
        startMatches = getBasketballMatchVos(startMatches);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("start",startMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesPasts(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        // get all past matches
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        LocalDate past = now.minusDays(6);
        Long pastSeconds = DateUtil.convertDateToLongTime(past);
        List<BasketballMatchVo> pastMatches = basketballMatchDao.getBasketballMatchesPast(pastSeconds,nowSeconds,limit,offset);
        pastMatches = getBasketballMatchVos(pastMatches);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("pass",pastMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesFuture(Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        // get all future matches in seven days
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        LocalDate future = now.plusDays(6);
        Long futureSeconds = DateUtil.convertDateToLongTime(future);
        List<BasketballMatchVo> futureMatches = basketballMatchDao.getBasketballMatchesFuture(nowSeconds,futureSeconds,limit,offset);
        futureMatches = getBasketballMatchVos(futureMatches);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("future",futureMatches);
        return results;
    }

    public List<BasketballMatchVo> getMatchListByDate(String date, Pageable pageable, String checkData) {
        long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        List<BasketballMatchVo> basketballMatchVos;
        // query today's matches do not start
        if(StringUtils.equals("true",checkData)){
            LocalDate now = LocalDate.now();
            Long nowSeconds = DateUtil.convertDateToLongTime(now);
            LocalDate tomorrow = now.plusDays(1);
            Long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
            basketballMatchVos = basketballMatchDao.getBasketballMatchNotStart(nowSeconds,tomorrowSeconds,limit,offset);
            basketballMatchVos = getBasketballMatchVos(basketballMatchVos);
        } else if(StringUtils.equals("false",checkData)) {
            LocalDate currentDate = DateUtil.convertStringToDate(date);
            LocalDate deadline = currentDate.plusDays(1);
            Long currentSeconds = DateUtil.convertDateToLongTime(currentDate);
            Long deadlineSeconds = DateUtil.convertDateToLongTime(deadline);
            basketballMatchVos = basketballMatchDao.getBasketballMatchFinished(currentSeconds,deadlineSeconds,limit,offset);
            basketballMatchVos = getBasketballMatchVos(basketballMatchVos);
        } else {
            LocalDate currentDate = DateUtil.convertStringToDate(date);
            LocalDate deadline = currentDate.plusDays(1);
            Long currentSeconds = DateUtil.convertDateToLongTime(currentDate);
            Long deadlineSeconds = DateUtil.convertDateToLongTime(deadline);
            basketballMatchVos = basketballMatchDao.getBasketballMatchByDate(currentSeconds,deadlineSeconds,limit,offset);
            basketballMatchVos = getBasketballMatchVos(basketballMatchVos);
        }
        return basketballMatchVos;
    }

    public BasketballMatchLineUpVo getBasketballMatchLineUpByMatchId(Long matchId) {
        BasketballMatchLineUpVo basketballMatchLineUpVo = new BasketballMatchLineUpVo();
        List<BasketballLineUp> home = basketballLineUpDao.getLineUpByMatchId(matchId, LineUpType.HOME.getType());
        List<BasketballLineUp> away = basketballLineUpDao.getLineUpByMatchId(matchId, LineUpType.AWAY.getType());
        basketballMatchLineUpVo.setHome(home);
        basketballMatchLineUpVo.setAway(away);
        return basketballMatchLineUpVo;
    }

    public BasketballMatchLiveDataVo getMatchLiveData(Long matchId) {
        BasketballMatchLiveDataVo basketballMatchLiveDataVo =  basketballMatchLiveDataDao.getMatchLiveDataByMatchId(matchId);
        if(basketballMatchLiveDataVo != null) {
            BasketballMatchVo basketballMatchVo = basketballMatchDao.getBasketBallMatchById(matchId);
            if(basketballMatchVo != null) {
                basketballMatchLiveDataVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchLiveDataVo.getStatus()));
                basketballMatchLiveDataVo.setMatchTimeStr(DateUtil.interceptTime(basketballMatchVo.getMatchTime()*1000));
                basketballMatchLiveDataVo.setMatchDate(DateUtil.convertLongTimeToMatchDate(basketballMatchVo.getMatchTime() * 1000));
                basketballMatchLiveDataVo.setHomeTeamName(basketballMatchVo.getHomeTeamName());
                basketballMatchLiveDataVo.setAwayTeamName(basketballMatchVo.getAwayTeamName());
                basketballMatchLiveDataVo.setHomeTeamLogo(basketballMatchVo.getHomeTeamLogo());
                basketballMatchLiveDataVo.setAwayTeamLogo(basketballMatchVo.getAwayTeamLogo());
            }
        }
        return basketballMatchLiveDataVo;
    }


    private List<BasketballMatchVo> getBasketballMatchVos(List<BasketballMatchVo> futureMatches) {
        if(futureMatches != null && !futureMatches.isEmpty()){
            Stream<BasketballMatchVo> basketballMatchVoStream = futureMatches.stream().map(vo ->{
                vo.setMatchTimeStr(DateUtil.interceptTime(vo.getMatchTime() * 1000));
                vo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(vo.getStatusId()));
                vo.setMatchDate(DateUtil.convertLongTimeToMatchDate(vo.getMatchTime() * 1000));
                return vo;
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(basketballMatchVoStream);
        }
        return futureMatches;
    }


}
