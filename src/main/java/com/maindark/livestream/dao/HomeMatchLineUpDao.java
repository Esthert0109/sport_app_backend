package com.maindark.livestream.dao;

import com.maindark.livestream.domain.HomeMatchLineUp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeMatchLineUpDao {

    @Insert("insert into home_match_line_up(player_id,match_id,team_id,first,captain,player_name," +
            "player_logo,shirt_number,position,rating,x,y)values("
            + "#{playerId},#{matchId},#{teamId},#{first},#{captain},#{playerName}," +
            "#{playerLogo},#{shirtNumber},#{position},#{rating},#{x},#{y})")
    Integer insert(HomeMatchLineUp homeMatchLineUp);


    @Update("update home_match_line_up set first=#{first},captain=#{captain},player_name=#{playerName},player_logo=#{playerLogo},shirt_number=#{shirtNumber},position=#{position},rating=#{rating},x=#{x},y=#{y} where player_id = #{playerId}")
    void updateHomeMatchLineUp(HomeMatchLineUp homeMatchLineUp);

    @Select("select * from home_match_line_up where id=#{id} and match_id=#{matchId}")
    HomeMatchLineUp getHomeMatchLineUp(@Param("id") Integer id, @Param("matchId") Integer matchId);

    @Select("select * from home_match_line_up where match_id=#{matchId}")
    List<HomeMatchLineUp> getHomeMatchLineUpByMatchId(@Param("matchId") Integer matchId);
}
