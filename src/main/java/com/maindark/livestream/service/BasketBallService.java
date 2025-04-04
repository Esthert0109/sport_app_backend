package com.maindark.livestream.service;


import com.maindark.livestream.dao.BasketballLineUpDao;
import com.maindark.livestream.dao.BasketballMatchDao;
import com.maindark.livestream.dao.BasketballMatchLiveDataDao;
import com.maindark.livestream.dao.FootballLiveAddressDao;
import com.maindark.livestream.domain.BasketballLineUp;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.LineUpType;
import com.maindark.livestream.txYun.TxYunConfig;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.BasketballMatchLineUpVo;
import com.maindark.livestream.vo.BasketballMatchLiveDataVo;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballLiveAddressVo;
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
public class BasketBallService {




    @Resource
    BasketballMatchDao basketballMatchDao;

    @Resource
    BasketballMatchLiveDataDao basketballMatchLiveDataDao;

    @Resource
    BasketballLineUpDao basketballLineUpDao;

    @Resource
    TxYunConfig txYunConfig;

    @Resource
    FootballLiveAddressDao footballLiveAddressDao;

    @Resource
    FollowService followService;



    public List<BasketballMatchVo> getBasketballMatchList(String search, Pageable pageable,Long userId) {
        long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        List<BasketballMatchVo> list = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        long nowSeconds = DateUtil.convertDateToLongTime(now);
        long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
        list = basketballMatchDao.getBasketballMatchByCompetitionName(search,nowSeconds,tomorrowSeconds,limit,offset);
        List<BasketballMatchVo> homeTeams =  basketballMatchDao.getBasketballMatchByHomeTeamName(search,nowSeconds,tomorrowSeconds,limit,offset);
        if(homeTeams != null && !homeTeams.isEmpty()) {
            list.addAll(homeTeams);
        }
        List<BasketballMatchVo> awayTeams = basketballMatchDao.getBasketballMatchByAwayTeamName(search,nowSeconds,tomorrowSeconds,limit,offset);
        if(awayTeams != null && !awayTeams.isEmpty()) {
            list.addAll(awayTeams);
        }
        if(list != null && !list.isEmpty()){
            Stream<BasketballMatchVo> stream = list.stream().filter(data ->data.getMatchTime() >= nowSeconds).peek(data ->{
                Long matchTime = data.getMatchTime() * 1000;
                String timeStr = DateUtil.interceptTime(matchTime);
                data.setMatchTimeStr(timeStr);
                data.setMatchDate(DateUtil.convertLongTimeToMatchDate(matchTime));
                Integer statusId = data.getStatusId();
                //data.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(statusId));
                data.setHomeTeamLogo(data.getHomeTeamLogo() == null?txYunConfig.getDefaultLogo():data.getHomeTeamLogo());
                data.setAwayTeamLogo(data.getAwayTeamLogo() == null?txYunConfig.getDefaultLogo():data.getAwayTeamLogo());
                if(userId != null) {
                    int matchId = data.getId().intValue();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                    data.setHasCollected(hasCollected);
                }
            });
            list = StreamToListUtil.getArrayListFromStream(stream);
        }
        return list;
    }



    public Map<String, List<BasketballMatchVo>> getBasketballMatchesStarts(Long userId,Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        // get all today's start matches
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        LocalDate tomorrow = now.plusDays(1);
        Long tomorrowSeconds = DateUtil.convertDateToLongTime(tomorrow);
        List<BasketballMatchVo> startMatches = basketballMatchDao.getBasketballMatchesStart(nowSeconds,tomorrowSeconds,limit,offset);
        startMatches = getBasketballMatchVos(userId,startMatches);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("start",startMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesPasts(Long userId,Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        // get all past matches
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        LocalDate past = now.minusDays(6);
        Long pastSeconds = DateUtil.convertDateToLongTime(past);
        List<BasketballMatchVo> pastMatches = basketballMatchDao.getBasketballMatchesPast(pastSeconds,nowSeconds,limit,offset);
        pastMatches = getBasketballMatchVos(userId,pastMatches);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("pass",pastMatches);
        return results;
    }

    public Map<String, List<BasketballMatchVo>> getBasketballMatchesFuture(Long userId,Pageable pageable) {
        Long offset = pageable.getOffset();
        Integer limit = pageable.getPageSize();
        // get all future matches in seven days
        LocalDate now = LocalDate.now();
        Long nowSeconds = DateUtil.convertDateToLongTime(now);
        LocalDate future = now.plusDays(6);
        Long futureSeconds = DateUtil.convertDateToLongTime(future);
        List<BasketballMatchVo> futureMatches = basketballMatchDao.getBasketballMatchesFuture(nowSeconds,futureSeconds,limit,offset);
        futureMatches = getBasketballMatchVos(userId,futureMatches);
        Map<String,List<BasketballMatchVo>> results = new HashMap<>();
        results.put("future",futureMatches);
        return results;
    }

    public List<BasketballMatchVo> getMatchListByDate(Long userId,String date, Pageable pageable, String checkData) {
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
            basketballMatchVos = getBasketballMatchVos(userId,basketballMatchVos);
        } else if(StringUtils.equals("false",checkData)) {
            LocalDate currentDate = DateUtil.convertStringToDate(date);
            LocalDate deadline = currentDate.plusDays(1);
            Long currentSeconds = DateUtil.convertDateToLongTime(currentDate);
            Long deadlineSeconds = DateUtil.convertDateToLongTime(deadline);
            basketballMatchVos = basketballMatchDao.getBasketballMatchFinished(currentSeconds,deadlineSeconds,limit,offset);
            basketballMatchVos = getBasketballMatchVos(userId,basketballMatchVos);
        } else {
            LocalDate currentDate = DateUtil.convertStringToDate(date);
            LocalDate deadline = currentDate.plusDays(1);
            Long currentSeconds = DateUtil.convertDateToLongTime(currentDate);
            Long deadlineSeconds = DateUtil.convertDateToLongTime(deadline);
            basketballMatchVos = basketballMatchDao.getBasketballMatchByDate(currentSeconds,deadlineSeconds,limit,offset);
            basketballMatchVos = getBasketballMatchVos(userId,basketballMatchVos);
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
                //basketballMatchLiveDataVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchLiveDataVo.getStatus()));
                basketballMatchLiveDataVo.setMatchTimeStr(DateUtil.interceptTime(basketballMatchVo.getMatchTime()*1000));
                basketballMatchLiveDataVo.setMatchDate(DateUtil.convertLongTimeToMatchDate(basketballMatchVo.getMatchTime() * 1000));
                basketballMatchLiveDataVo.setHomeTeamName(basketballMatchVo.getHomeTeamName());
                basketballMatchLiveDataVo.setAwayTeamName(basketballMatchVo.getAwayTeamName());
                basketballMatchLiveDataVo.setHomeTeamLogo(basketballMatchLiveDataVo.getHomeTeamLogo() == null?txYunConfig.getDefaultLogo():basketballMatchLiveDataVo.getHomeTeamLogo());
                basketballMatchLiveDataVo.setAwayTeamLogo(basketballMatchLiveDataVo.getAwayTeamLogo() == null?txYunConfig.getDefaultLogo():basketballMatchLiveDataVo.getAwayTeamLogo());
            }
        }
        return basketballMatchLiveDataVo;
    }


    private List<BasketballMatchVo> getBasketballMatchVos(Long userId,List<BasketballMatchVo> futureMatches) {
        if(futureMatches != null && !futureMatches.isEmpty()){
            Stream<BasketballMatchVo> basketballMatchVoStream = futureMatches.stream().peek(vo ->{
                vo.setMatchTimeStr(DateUtil.interceptTime(vo.getMatchTime() * 1000));
                //vo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(vo.getStatusId()));
                vo.setMatchDate(DateUtil.convertLongTimeToMatchDate(vo.getMatchTime() * 1000));
                vo.setHomeTeamLogo(vo.getHomeTeamLogo() == null?txYunConfig.getDefaultLogo():vo.getHomeTeamLogo());
                vo.setAwayTeamLogo(vo.getAwayTeamLogo() == null?txYunConfig.getDefaultLogo():vo.getAwayTeamLogo());
                if(userId != null) {
                    int matchId = vo.getId().intValue();
                    Boolean hasCollected = followService.hasFollowed(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(), matchId);
                    vo.setHasCollected(hasCollected);
                }
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(basketballMatchVoStream);
        }
        return futureMatches;
    }


    public FootballLiveAddressVo getBasketballLiveAddressByMatchId(Integer matchId) {
        return footballLiveAddressDao.getLiveAddressByMatchId(matchId,2);
    }
}
