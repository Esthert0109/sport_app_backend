package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FeiJingFootballMatchDao {
    @Insert("insert into fei_jing_football_match(match_id, season, kind, competition_id, league_en, league_en_short, " +
            "league_cns_short, status_id, match_time, match_date, home_team_id, away_team_id, " +
            "home_team_name_en, home_team_name_cn, away_team_name_en, away_team_name_cn, " +
            "home_team_logo, away_team_logo, home_team_score, away_team_score, home_formation, " +
            "away_formation, venue, line_up, updated_at) values(#{matchId},#{season},#{kind},#{competitionId},#{leagueEn}" +
            ",#{leagueEnShort},#{leagueCnsShort},#{statusId},#{matchTime},#{matchDate},#{homeTeamId}," +
            "#{awayTeamId},#{homeTeamNameEn},#{homeTeamNameCn},#{awayTeamNameEn},#{awayTeamNameCn},#{homeTeamLogo}," +
            "#{awayTeamLogo},#{homeTeamScore},#{awayTeamScore},#{homeFormation},#{awayFormation},#{venue},#{lineUp},#{updatedTime})")
    void insertData(FeiJingFootballMatch feiJingFootballMatch);

    @Select("select count(1) from fei_jing_football_match where match_id=#{matchId}")
    int  queryExisted(@Param("matchId")Integer matchId);

    void updateMatchScoreByMatchId(FeiJingFootballMatch feiJingFootballMatch);
    @Update("update fei_jing_football_match set home_formation = #{homeFormation},away_formation=#{awayFormation} where match_id=#{matchId}")
    void updateFormationByMatchId(@Param("homeFormation") String homeFormation, @Param("awayFormation") String awayFormation, @Param("matchId") Integer matchId);
}
