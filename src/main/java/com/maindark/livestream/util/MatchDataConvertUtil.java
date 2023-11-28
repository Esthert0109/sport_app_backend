package com.maindark.livestream.util;

import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;

import java.util.List;
import java.util.stream.Stream;

public class MatchDataConvertUtil {

    public static List<BasketballMatchVo> getBasketballMatchVos(List<BasketballMatchVo> futureMatches) {
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

    public static List<FootballMatchVo> getFootballMatchVos(List<FootballMatchVo> futureMatches) {
        if(futureMatches != null && !futureMatches.isEmpty()){
            Stream<FootballMatchVo> footballMatchVoStream = futureMatches.stream().map(vo ->{
                vo.setStatusStr(FootballMatchStatus.convertStatusIdToStr(vo.getStatusId()));
                vo.setMatchTimeStr(DateUtil.interceptTime(vo.getMatchTime() * 1000));
                vo.setMatchDate(DateUtil.convertLongTimeToMatchDate(vo.getMatchTime() * 1000));
                return vo;
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(footballMatchVoStream);
        }
        return futureMatches;
    }
}
