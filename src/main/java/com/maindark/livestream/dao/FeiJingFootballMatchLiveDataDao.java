package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingFootballMatchLiveData;
import com.maindark.livestream.vo.FeiJingFootballMatchLiveDataVo;
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
            "#{awayAttackDangerNum},#{homePossessionRate},#{awayPossessionRate},#{homeShootGoalNum},#{awayShootGoalNum},#{homeBiasNum},#{awayBiasNum}," +
            "#{homeCornerKickNum},#{awayCornerKickNum},#{homeRedCardNum},#{awayRedCardNum},#{homeYellowCardNum},#{awayYellowCardNum})")
    void insertData(FeiJingFootballMatchLiveData feiJingFootballMatchLiveData);

    @Update("update fei_jing_football_match_live_data set home_attack_num=#{homeAttackNum},away_attack_num=#{awayAttackNum}," +
            " home_attack_danger_num=#{homeAttackDangerNum},away_attack_danger_num=#{awayAttackDangerNum},home_possession_rate=#{homePossessionRate}," +
            "away_possession_rate=#{awayPossessionRate},home_shoot_goal_num=#{homeShootGoalNum},away_shoot_goal_num=#{awayShootGoalNum}," +
            "home_bias_num=#{homeBiasNum},away_bias_num=#{awayBiasNum},home_corner_kick_num=#{homeCornerKickNum},away_corner_kick_num=#{awayCornerKickNum}," +
            "home_red_card_num=#{homeRedCardNum},away_red_card_num=#{awayRedCardNum},home_yellow_card_num=#{homeYellowCardNum},away_yellow_card_num=#{awayYellowCardNum} where match_id=#{matchId}")
    void updateData(FeiJingFootballMatchLiveData feiJingFootballMatchLiveData);
    @Select("select t.match_id,m.status_id as status,m.match_date,m.match_time,m.home_team_name_cn as home_team_name," +
            "m.away_team_name_cn as away_team_name,m.home_team_logo,m.away_team_logo,m.venue as venueName," +
            "m.home_formation,m.away_formation,m.home_coach,m.away_coach," +
            "m.home_team_score as home_score,m.away_team_score as away_score,t.home_attack_num," +
            "t.away_attack_num,t.home_attack_danger_num,t.away_attack_danger_num," +
            "t.home_possession_rate,t.away_possession_rate,t.home_shoot_goal_num,t.away_shoot_goal_num," +
            "t.home_bias_num,t.away_bias_num,t.home_corner_kick_num,t.away_corner_kick_num,t.home_red_card_num,t.away_red_card_num," +
            "t.home_yellow_card_num,t.away_yellow_card_num from fei_jing_football_match_live_data t inner join fei_jing_football_match m on t.match_id= m.match_id where t.match_id=#{matchId}")
    FeiJingFootballMatchLiveDataVo getMatchLiveData(Integer matchId);

}
