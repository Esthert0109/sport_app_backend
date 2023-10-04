package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballLiveAddress;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballLiveAddressDao {

    @Insert("insert into football_live_address(id, match_id, match_time, comp, home_team, away_team, mobile_link, pc_link)values(#{matchId},#{matchTime},#{comp},#{homeTeam},#{awayTeam},#{mobileLink},#{pcLink})")
    public Integer insert(FootballLiveAddress footballLiveAddress);

    @Select("select * from football_live_address where match_id=#{matchId}")
    public FootballLiveAddress getFootballLiveAddressByMatchId(@Param("matchId")Integer matchId);

    @Update("update football_live_address set match_time=#{matchTime},comp=#{comp},home_team=#{homeTeam},away_team=#{awayTeam},mobile_link=#{mobileLink},pc_link=#{pcLink} where match_id=#{matchId}")
    public void updateFootballLiveAddressByMatchId(FootballLiveAddress footballLiveAddress);


}
