package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballLiveAddress;
import com.maindark.livestream.vo.FootballLiveAddressVo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballLiveAddressDao {

    @Insert("insert into football_live_address(sport_id,match_id,match_time,match_status,comp_id,comp,home_team, " +
            "away_team,push_url1,push_url2,push_url3" +
            ")values(#{sportId},#{matchId},#{matchTime},#{matchStatus},#{compId},#{comp},#{homeTeam},#{awayTeam}," +
            "#{pushUrl1},#{pushUrl2},#{pushUrl3})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    Integer insert(FootballLiveAddress footballLiveAddress);

    @Select("select * from football_live_address where match_id=#{matchId}")
    FootballLiveAddress getFootballLiveAddressByMatchId(@Param("matchId") Integer matchId);

    @Update("update football_live_address set match_id=#{matchId},match_time=#{matchTime},match_status=#{matchStatus},comp_id=#{compId}" +
            "comp=#{comp},home_team=#{homeTeam},away_team=#{awayTeam},push_url1=#{pushUrl1},push_url2=#{pushUrl2},push_url3=#{pushUrl3}" +
            " where match_id=#{matchId}")
    void updateFootballLiveAddressByMatchId(FootballLiveAddress footballLiveAddress);

    @Select("select sport_id,match_id,push_url1,push_url2,push_url3 from football_live_address where sport_id=#{sportid} and match_id=#{matchId}")
    FootballLiveAddressVo getLiveAddressByMatchId(@Param("matchId") Integer matchId,@Param("sportId")Integer sportId);


}
