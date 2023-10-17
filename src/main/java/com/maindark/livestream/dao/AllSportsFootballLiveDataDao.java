package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballMatchLiveData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AllSportsFootballLiveDataDao {
    @Insert("insert into all_sports_football_match_live_data(match_id, status, match_time, match_date, home_team_name, away_team_name, " +
            "home_team_logo, away_team_logo, home_attack_num, away_attack_num, home_attack_danger_num, away_attack_danger_num, home_possession_rate, " +
            "away_possession_rate, home_shoot_goal_num, away_shoot_goal_num, home_bias_num, away_bias_num, home_corner_kick_num, away_corner_kick_num, " +
            "home_red_card_num, away_red_card_num, home_yellow_card_num, away_yellow_card_num, home_score, away_score) values (" +
            "#{matchId},#{status},#{matchTime},#{matchDate},#{homeTeamName},#{awayTeamName},#{homeTeamLogo},#{awayTeamLogo},#{HomeAttackNum}" +
            "#{awayAttackNum},#{homeAttackDangerNum},#{awayAttackDangerNum},#{homePossessionRate},#{awayPossessionRate},#{homeShootGoalNum}," +
            "#{awayShootGoalNum},#{homeBiasNum},#{awayBiasNum},#{homeCornerKickNum},#{awayCornerKickNum},#{homeRedCardNum},#{awayRedCardNum}," +
            "#{homeYellowCardNum},#{awayYellowCardNum},#{homeScore},#{awayScore})")
    Integer insert(AllSportsFootballMatchLiveData footballMatchLiveData);

    @Update("update all_sports_football_match_live_data set status=#{status},home_attack_num=#{homeAttackNum},away_attack_num=#{awayAttackNum}," +
            "home_possession_rate=#{homePossessionRate},away_possession_rate=#{awayPossessionRate}," +
            "home_shoot_goal_num=#{homeShootGoalNum},away_shoot_goal_num=#{awayShootGoalNum},home_bias_num=#{homeBiasNum}," +
            "away_bias_num=#{awayBiasNum},home_corner_kick_num=#{homeCornerKickNum},away_corner_kick_num=#{awayCornerKickNum}," +
            "home_red_card_num=#{homeRedCardNum},away_red_card_num=#{awayRedCardNum},home_yellow_card_num=#{homeYellowCardNum}," +
            "away_yellow_card_num=#{awayYellowCardNum},home_score=#{homeScore},away_score=#{awayScore} where match_id=#{matchId}")
    void updateData(AllSportsFootballMatchLiveData allSportsFootballMatchLiveData);

    @Select("select count(1) from all_sports_football_match_live_data where match_id=#{matchId}")
    int queryExist(@Param("matchId")Long matchId);


}
