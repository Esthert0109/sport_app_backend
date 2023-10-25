package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AwayMatchLineUp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AwayMatchLineUpDao {

    @Insert("insert into away_match_line_up(player_id,match_id,team_id,first,captain,player_name,player_logo,shirt_number,position,rating,x,y)values("
            + "#{playerId},#{matchId},#{teamId},#{first},#{captain},#{playerName},#{playerLogo},#{shirtNumber},#{position},#{rating},#{x},#{y})")
    Integer insert(AwayMatchLineUp awayMatchLineUp);

    @Update("update away_match_line_up set first=#{first},captain=#{captain},player_name=#{playerName},player_logo=#{playerLogo},shirt_number=#{shirtNumber},position=#{position},rating=#{rating},x=#{x},y=#{y} where player_id = #{playerId}")
    void updateAwayMatchLineUp(AwayMatchLineUp awayMatchLineUp);

    @Select("select * from away_match_line_up where id=#{id} and match_id=#{matchId}")
    AwayMatchLineUp getAwayMatchLineUp(@Param("id") Integer id, @Param("matchId") Integer matchId);

    @Select("select * from away_match_line_up where match_id=#{matchId}")
    List<AwayMatchLineUp> getAwayMatchLineUpByMatchId(@Param("matchId") Integer matchId);
}

