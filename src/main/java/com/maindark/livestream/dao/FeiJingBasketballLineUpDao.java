package com.maindark.livestream.dao;

import com.maindark.livestream.domain.BasketballLineUp;
import com.maindark.livestream.domain.feijing.FeiJingBasketballLineUp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeiJingBasketballLineUpDao {
    @Insert("insert into fei_jing_basketball_line_up(match_id, type,player_id, player_name,minutes, point, assists, steals, total_rebounds, " +
            "free_throws_goals_attempts, free_throws_goals_made, personal_fouls, turnovers, three_point_goals_attempts, " +
            "three_point_goals_made, blocks, field_goals_attempts, field_goals_made) VALUES (#{matchId}," +
            "#{type},#{playerId},#{playerName},#{minutes},#{point},#{assists},#{steals},#{totalRebounds}," +
            "#{freeThrowsGoalsAttempts},#{freeThrowsGoalsMade}," +
            "#{personalFouls},#{turnovers},#{threePointGoalsAttempts},#{threePointGoalsMade},#{blocks}," +
            "#{fieldGoalsAttempts},#{fieldGoalsMade})")
    void insertData(FeiJingBasketballLineUp basketballLineUp);


    @Update("update fei_jing_basketball_line_up set minutes=#{minutes},point=#{point},assists=#{assists},steals=#{steals}," +
            "total_rebounds=#{totalRebounds},free_throws_goals_attempts=#{freeThrowsGoalsAttempts}," +
            "free_throws_goals_made=#{freeThrowsGoalsMade},personal_fouls=#{personalFouls},turnovers=#{turnovers}," +
            "three_point_goals_attempts=#{threePointGoalsAttempts}," +
            "three_point_goals_made=#{threePointGoalsMade},blocks=#{blocks}," +
            "field_goals_attempts=#{fieldGoalsAttempts},field_goals_made=#{fieldGoalsMade} where player_id=#{playerId} and match_id=#{matchId}")
    void updateData(FeiJingBasketballLineUp basketballLineUp);

    @Select("select count(1) from fei_jing_basketball_line_up where player_id=#{playerId} and match_id=#{matchId}")
    int queryExist(@Param("playerId")Integer playerId, @Param("matchId")Integer matchId);
    @Select("select * from fei_jing_basketball_line_up where type=#{type} and match_id=#{matchId}")
    List<BasketballLineUp> getLineUpByMatchId(@Param("matchId") Long matchId, @Param("type") Integer type);
}
