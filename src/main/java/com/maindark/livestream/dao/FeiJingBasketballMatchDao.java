package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingBasketballMatch;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingBasketballMatchDao {

    @Insert("insert into fei_jing_basketball_match(match_id, competition_id, competition_name, home_team_id, away_team_id, home_team_name, " +
            "home_team_cns, away_team_name, away_team_cns, " +
            "home_team_logo, away_team_logo, status, match_time, " +
            "match_date, h_f_quarter, h_s_quarter, h_t_quarter, " +
            "h_4_quarter, a_f_quarter, a_s_quarter, a_t_quarter, " +
            "a_4_quarter, home_score, away_score, home_coach, " +
            "away_coach, has_state, season) values(" +
            "#{matchId},#{competitionId},#{competitionName},#{homeTeamId},#{awayTeamId},#{homeTeamName}," +
            "#{homeTeamCns},#{awayTeamName},#{awayTeamCns},#{homeTeamLogo},#{awayTeamLogo},#{status},#{matchTime}," +
            "#{matchDate},#{hFQuater},#{hSQuater},#{hTQuater},#{h4Quater}," +
            "#{aFQuater},#{aSQuater},#{aTQuater},#{a4Quater},#{homeScore},#{awayScore},#{homeCoach}," +
            "#{awayCoach},#{hasState},#{season})")
    void insertData(FeiJingBasketballMatch feiJingBasketballMatch);
    @Select("select count(1) from fei_jing_basketball_match where match_id=#{matchId}")
    int queryExisted(@Param("matchId") Integer matchId);

    void updateMatchScoreByMatchId(FeiJingBasketballMatch feiJingBasketballMatch);
}
