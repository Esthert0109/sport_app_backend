package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AwayMatchLineUp;
import com.maindark.livestream.domain.HomeMatchLineUp;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AwayMatchLineUpDao {

    @Insert("insert into away_match_line_up(id,match_id,team_id,first,captain,player_name,player_logo,shirt_number,position,rating)values("
            + "#{id},#{matchId},#{teamId},#{first},#{captain},#{playerName},#{playerLogo},#{shirtNumber},#{position},#{rating})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    public Integer insert(AwayMatchLineUp awayMatchLineUp);

    @Update("update away_match_line_up set first=#{first},captain=#{captain},player_name=#{playerName},player_logo=#{playerLogo},shirt_number=#{shirtNumber},position=#{position},rating=#{rating} where match_id = #{matchId}")
    public void updateAwayMatchLineUp(AwayMatchLineUp awayMatchLineUp);

    @Select("select * from away_match_line_up where id=#{id} and match_id=#{matchId}")
    public AwayMatchLineUp getAwayMatchLineUp(@Param("id")Integer id,@Param("matchId")Integer matchId);
}

