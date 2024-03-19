package com.maindark.livestream.util;

import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

public class MatchDataConvertUtil {
    public static final String logo = "https://live-stream-1321239144.cos.ap-singapore.myqcloud.com/head/e60f869e80314ffb8ce2b89913f384fc.jpg";

    public static List<BasketballMatchVo> getBasketballMatchVos(List<BasketballMatchVo> futureMatches) {
        if(futureMatches != null && !futureMatches.isEmpty()){
            Stream<BasketballMatchVo> basketballMatchVoStream = futureMatches.stream().map(vo ->{
                //vo.setMatchTimeStr(DateUtil.interceptTime(vo.getMatchTime() * 1000));
                vo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(vo.getStatus()));
                //vo.setMatchDate(DateUtil.convertLongTimeToMatchDate(vo.getMatchTime() * 1000));
                vo.setHomeTeamLogo(vo.getHomeTeamLogo() == null?logo:vo.getHomeTeamLogo());
                vo.setAwayTeamLogo(vo.getAwayTeamLogo() == null?logo:vo.getAwayTeamLogo());
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
                //vo.setMatchTimeStr(DateUtil.interceptTime(vo.getMatchTime() * 1000));
                //vo.setMatchDate(DateUtil.convertLongTimeToMatchDate(vo.getMatchTime() * 1000));
                vo.setHomeTeamLogo(vo.getHomeTeamLogo() == null?logo:vo.getHomeTeamLogo());
                vo.setAwayTeamLogo(vo.getAwayTeamLogo() == null?logo:vo.getAwayTeamLogo());
                return vo;
            });
            futureMatches = StreamToListUtil.getArrayListFromStream(footballMatchVoStream);
        }
        return futureMatches;
    }
}
