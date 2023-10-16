package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsAwayMatchLineUp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AllSportsAwayMatchLineUpDao {

    @Insert("insert into all_sports_away_match_line_up(id, match_id, team_id, first, captain, player_name, player_logo, shirt_number, position, rating) values (" +
            "#{id},#{matchId},#{teamId},#{first},#{captain},#{playerName},#{playerLogo},#{shirtNumber},#{position},#{rating})")
    Integer insert(AllSportsAwayMatchLineUp allSportsAwayMatchLineUp);

    @Select("select count(1) from all_sports_away_match_line_up where id=#{id}")
    int queryExists(@Param("id")Long id);
}
