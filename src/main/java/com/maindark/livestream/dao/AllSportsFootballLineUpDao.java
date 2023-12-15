package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballLineUp;
import com.maindark.livestream.domain.AllSportsHomeMatchLineUp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AllSportsFootballLineUpDao {

    @Insert("insert into all_sports_football_line_up(type,player_id, match_id, team_id, first, captain, player_name, player_logo, shirt_number, position, rating) values (" +
            "#{type},#{playerId},#{matchId},#{teamId},#{first},#{captain},#{playerName},#{playerLogo},#{shirtNumber},#{position},#{rating})")
    Integer insert(AllSportsFootballLineUp allSportsFootballLineUp);

    @Select("select count(1) from all_sports_football_line_up where player_id=#{playerId} and match_id=#{matchId}")
    int queryExists(@Param("playerId")Long playerId, @Param("matchId")Long matchId);

    @Select("select * from all_sports_football_line_up where type= 0 and  match_id=#{matchId}")
    List<AllSportsFootballLineUp> getHomeMatchLineUpByMatchId(@Param("matchId") Long matchId);

    @Select("select * from all_sports_football_line_up where type= 1 and  match_id=#{matchId}")
    List<AllSportsFootballLineUp> getAwayMatchLineUpByMatchId(@Param("matchId") Long matchId);
    @Update("update all_sports_football_line_up set rating=#{rating} where player_id=#{playerId} and match_id=#{matchId} ")
    void updateMatchLineUp(AllSportsFootballLineUp allSportsFootballLineUp);

}
