package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballMatchLiveData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballMatchLiveDataDao {

    @Insert("insert into football_match_live_data(match_id,status_id,home_team_id,away_team_id," +
            "home_team_name,away_team_name,home_attack_num,away_attack_num,home_attack_danger_num,away_attack_danger_num,home_possession_rate,away_possession_rate," +
            "home_shoot_goal_num,away_shoot_goal_num,home_bias_num,away_bias_num,home_corner_kick_num,away_corner_kick_num,home_red_card_num,away_red_card_num," +
            "home_yellow_card_num,away_yellow_card_num,home_score,away_score" +
            " )values("
            + "#{matchId}, #{statusId}, #{homeTeamId}, #{awayTeamId}, #{homeTeamName}," +
            "#{awayTeamName},#{homeAttackNum},#{awayAttackNum},#{homeAttackDangerNum},#{awayAttackDangerNum},#{homePossessionRate},#{awayPossessionRate},#{homeShootGoalNum}" +
            ",#{awayShootGoalNum},#{homeBiasNum},#{awayBiasNum},#{homeCornerKickNum},#{awayCornerKickNum},#{homeRedCardNum},#{awayRedCardNum},#{homeYellowCardNum}," +
            "#{awayYellowCardNum},#{homeScore},#{awayScore})")
    public Integer insert(FootballMatchLiveData footballMatchLiveData);





    @Select("select * from football_match_live_data where match_id = #{matchId}")
    FootballMatchLiveData getFootballMatchLiveData(@Param("matchId") Integer matchId);

    @Update("update football_match_live_data set status_id=#{statusId},home_attack_num=#{homeAttackNum},away_attack_num=#{awayAttackNum}," +
            "home_attack_danger_num=#{homeAttackDangerNum},away_attack_danger_num=#{awayAttackDangerNum}," +
            "home_possession_rate=#{homePossessionRate},away_possession_rate=#{awayPossessionRate},home_shoot_goal_num=#{homeShootGoalNum},away_shoot_goal_num=#{awayShootGoalNum}," +
            "home_bias_num=#{homeBiasNum},away_bias_num=#{awayBiasNum},home_corner_kick_num=#{homeCornerKickNum},away_corner_kick_num=#{awayCornerKickNum}, " +
            "home_red_card_num=#{homeRedCardNum},away_red_card_num=#{awayRedCardNum},home_yellow_card_num=#{homeYellowCardNum}," +
            "away_yellow_card_num=#{awayYellowCardNum},home_score=#{homeScore},away_score=#{awayScore} where match_id=#{matchId}")
    public void updateFootballMatchLiveData(FootballMatchLiveData footballMatchLiveData);



}
