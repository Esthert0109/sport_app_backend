package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsAwayMatchLineUp;
import com.maindark.livestream.domain.AllSportsHomeMatchLineUp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AllSportsHomeMatchLineUpDao {

    @Insert("insert into all_sports_home_match_line_up(id, match_id, team_id, first, captain, player_name, player_logo, shirt_number, position, rating) values (" +
            "#{id},#{matchId},#{teamId},#{first},#{captain},#{playerName},#{playerLogo},#{shirtNumber},#{position},#{rating})")
    Integer insert(AllSportsHomeMatchLineUp allSportsAwayMatchLineUp);

    @Select("select count(1) from all_sports_home_match_line_up where id=#{id}")
    int queryExists(@Param("id")Long id);

    @Select("select * from all_sports_home_match_line_up where match_id=#{matchId}")
    List<AllSportsHomeMatchLineUp> getHomeMatchLineUpByMatchId(@Param("matchId") Long matchId);
}
