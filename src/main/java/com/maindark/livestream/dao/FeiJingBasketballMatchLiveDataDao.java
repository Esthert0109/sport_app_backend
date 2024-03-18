package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsBasketballMatchLiveData;
import com.maindark.livestream.domain.feijing.FeiJingBasketballMatchLiveData;
import com.maindark.livestream.vo.AllSportsBasketballLiveDataVo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FeiJingBasketballMatchLiveDataDao {
    @Insert("insert into fei_jing_basketball_match_live_data(match_id," +
            "h_blocks, h_field_goals, " +
            "h_free_throws, h_personal_fouls, h_rebounds, h_steals, h_three_point_goals, " +
            "h_turn_overs, a_blocks, a_field_goals, a_free_throws, a_personal_fouls, a_rebounds," +
            " a_steals, a_three_point_goals, a_turn_overs) VALUES (" +
            "#{matchId}," +
            "#{hBlocks},#{hFieldGoals},#{hFreeThrows},#{hPersonalFouls},#{hRebounds},#{hSteals}," +
            "#{hThreePointGoals},#{hTurnOvers},#{aBlocks},#{aFieldGoals},#{aFreeThrows},#{aPersonalFouls},#{aRebounds}," +
            "#{aSteals},#{aThreePointGoals},#{aTurnOvers})")
    Integer insertData(FeiJingBasketballMatchLiveData feiJingBasketballMatchLiveData);


    @Update("update fei_jing_basketball_match_live_data set " +
            "h_blocks=#{hBlocks},h_field_goals=#{hFieldGoals},h_free_throws=#{hFreeThrows},h_rebounds=#{hRebounds},h_three_point_goals=#{hThreePointGoals}" +
            ",h_personal_fouls=#{hPersonalFouls},h_steals=#{hSteals},h_turn_overs=#{hTurnOvers}," +
            "a_blocks=#{aBlocks},a_field_goals=#{aFieldGoals},a_free_throws=#{aFreeThrows},a_personal_fouls=#{aPersonalFouls},a_steals=#{aSteals}," +
            "a_turn_overs=#{aTurnOvers},a_rebounds=#{aRebounds},a_three_point_goals=#{aThreePointGoals} where match_id=#{matchId}")
    void updateData(FeiJingBasketballMatchLiveData feiJingBasketballMatchLiveData);

    @Select("select count(1) from fei_jing_basketball_match_live_data where match_id=#{matchId}")
    int queryExist(@Param("matchId")Integer matchId);


    @Select("select match_id," +
            "h_blocks,h_field_goals,h_free_throws,h_personal_fouls,h_rebounds,h_three_point_goals,h_rebounds,h_turn_overs, " +
            "a_blocks,a_field_goals,a_free_throws,a_personal_fouls,a_rebounds,a_three_point_goals,a_rebounds,a_turn_overs " +
            "from fei_jing_basketball_match_live_data where match_id=#{matchId}")
    AllSportsBasketballLiveDataVo getMatchLiveDataByMatchId(@Param("matchId") Integer matchId);
}
