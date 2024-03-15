package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingFootballMatchLiveData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FeiJingFootballMatchLiveDataDao {
    @Select("select count(1) from fei_jing_football_match_live_data where match_id=#{matchId}")
    int queryExisted(@Param("matchId")Integer matchId);

    @Insert("insert into fei_jing_football_match_live_data (match_id, home_attack_num, away_attack_num, home_attack_danger_num, " +
            "away_attack_danger_num, home_possession_rate, away_possession_rate, " +
            "home_shoot_goal_num, away_shoot_goal_num, home_bias_num, away_bias_num, " +
            "home_corner_kick_num, away_corner_kick_num, home_red_card_num, away_red_card_num, " +
            "home_yellow_card_num, away_yellow_card_num) values(#{matchId},#{homeAttackNum},#{awayAttackNum},#{homeAttackDangerNum}," +
            "#{awayAttackDangerNum},#{homePosessionRate},#{awayPossessionRate},#{homeShootGoalNum},#{awayHomeShootGoalNum},#{homeBiasNum},#{awayBiasNum}," +
            "#{homeCornerKickNum},#{awayCornerKickNum},#{homeRedCardNum},#{awayRedCardNum},#{homeYellowCardNum},#{awayYellowCardNum})")
    void insertData(FeiJingFootballMatchLiveData feiJingFootballMatchLiveData);

    @Update("update fei_jing_football_match_live_data set home_attack_num=#{homeAttackNum},away_attack_num=#{awayAttackNum}," +
            " home_attack_danger_num=#{homeAttackDangerNum},away_attack_danger_num=#{awayAttackDangerNum},home_possession_rate=#{homePosessionRate}," +
            "away_possession_rate=#{awayPossessionRate},home_shoot_goal_num=#{homeShootGoalNum},away_shoot_goal_num=#{awayHomeShootGoalNum}," +
            "home_bias_num=#{homeBiasNum},away_bias_num=#{awayBiasNum},home_corner_kick_num=#{homeCornerKickNum},away_corner_kick_num=#{awayCornerKickNum}," +
            "home_red_card_num=#{homeRedCardNum},away_red_card_num=#{awayRedCardNum},home_yellow_card_num=#{homeYellowCardNum},away_yellow_card_num=#{awayYellowCardNum} where match_id=#{matchId}")
    void updateData(FeiJingFootballMatchLiveData feiJingFootballMatchLiveData);
}
