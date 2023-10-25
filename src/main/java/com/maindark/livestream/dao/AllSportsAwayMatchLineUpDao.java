package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsAwayMatchLineUp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AllSportsAwayMatchLineUpDao {

    @Insert("insert into all_sports_away_match_line_up(player_id, match_id, team_id, first, captain, player_name, player_logo, shirt_number, position, rating) values (" +
            "#{playerId},#{matchId},#{teamId},#{first},#{captain},#{playerName},#{playerLogo},#{shirtNumber},#{position},#{rating})")
    Integer insert(AllSportsAwayMatchLineUp allSportsAwayMatchLineUp);

    @Select("select count(1) from all_sports_away_match_line_up where player_id=#{playerId} and match_id=#{matchId}")
    int queryExists(@Param("playerId")Long playerId,@Param("matchId")Long matchId);

    @Select("select * from all_sports_away_match_line_up where match_id=#{matchId}")
    List<AllSportsAwayMatchLineUp> getAwayMatchLineUpByMatchId(@Param("matchId") Long matchId);
}
