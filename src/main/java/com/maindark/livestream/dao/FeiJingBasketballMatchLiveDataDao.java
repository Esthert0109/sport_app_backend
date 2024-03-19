package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingBasketballMatchLiveData;
import com.maindark.livestream.vo.FeiJingBasketballLiveDataVo;
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

    @Select("select m.match_id,b.home_team_cns as home_team_name,b.away_team_cns as away_team_name," +
            "b.home_team_logo,b.away_team_logo,b.home_score,b.away_score,b.match_date,b.match_time,b.status as status_str," +
            "b.h_f_quarter,b.h_s_quarter,b.h_t_quarter,b.h_4_quarter,b.a_f_quarter,b.a_s_quarter,b.a_t_quarter,b.a_4_quarter," +
            "m.h_blocks,m.h_field_goals,m.h_free_throws,m.h_personal_fouls,m.h_rebounds,m.h_three_point_goals,m.h_rebounds,m.h_turn_overs,m.h_steals, " +
            "m.a_blocks,m.a_field_goals,m.a_free_throws,m.a_personal_fouls,m.a_rebounds,m.a_three_point_goals,m.a_rebounds,m.a_turn_overs,m.a_steals " +
            "from fei_jing_basketball_match_live_data m inner join fei_jing_basketball_match b on m.match_id= b.match_id where m.match_id=#{matchId}")
    FeiJingBasketballLiveDataVo getMatchLiveDataByMatchId(@Param("matchId") Integer matchId);
}
