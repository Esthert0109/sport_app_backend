package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballLineUp;
import com.maindark.livestream.domain.feijing.FeiJingFootballLineUp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeiJingFootballLineUpDao {

    @Insert("insert into fei_jing_football_line_up(type,player_id, match_id, first, captain, player_name, shirt_number, position, rating) values (" +
            "#{type},#{playerId},#{matchId},#{first},#{captain},#{playerName},#{shirtNumber},#{position},#{rating})")
    Integer insert(FeiJingFootballLineUp footballLineUp);

    @Select("select count(1) from fei_jing_football_line_up where player_id=#{playerId} and match_id=#{matchId}")
    int queryExists(@Param("playerId")Integer playerId, @Param("matchId")Integer matchId);

    @Select("select * from fei_jing_football_line_up where type= 0 and  match_id=#{matchId}")
    List<FeiJingFootballLineUp> getHomeMatchLineUpByMatchId(@Param("matchId") Integer matchId);

    @Select("select * from fei_jing_football_line_up where type= 1 and  match_id=#{matchId}")
    List<FeiJingFootballLineUp> getAwayMatchLineUpByMatchId(@Param("matchId") Integer matchId);
    @Update("update fei_jing_football_line_up set rating=#{rating} where player_id=#{playerId} and match_id=#{matchId} ")
    void updateMatchLineUp(FeiJingFootballLineUp allSportsFootballLineUp);

}
