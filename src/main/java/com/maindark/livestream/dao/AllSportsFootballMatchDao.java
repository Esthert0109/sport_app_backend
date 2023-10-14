package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballMatch;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AllSportsFootballMatchDao {

    @Insert("insert into all_sports_football_match(id, competition_id, competition_name,status, match_time, match_date, home_team_id, away_team_id, " +
            "home_team_name, away_team_name, home_team_logo, away_team_logo, home_team_score, away_team_score, home_formation, " +
            "away_formation, referee_name,venue_name,event_live, line_up)values (" +
            "#{id},#{competitionId},#{competitionName},#{status},#{matchTime},#{matchDate},#{homeTeamId},#{awayTeamId}," +
            "#{homeTeamName},#{awayTeamName},#{homeTeamLogo},#{awayTeamLogo},#{homeTeamScore},#{awayTeamScore},#{homeFormation}" +
            ",#{awayFormation},#{refereeName},#{venueName},#{eventLive},#{lineUp})")
    Integer insert(AllSportsFootballMatch allSportsFootballMatch);

    @Select("select count(1) from all_sports_football_match where id = #{id} ")
    int queryMatchIsExists(@Param("id")Long id);

    @Update("update all_sports_football_match set status=#{status},event_live=#{eventLive},line_up=#{lineUp} where id = #{id}")
    void updateAllSportsMatch(AllSportsFootballMatch allSportsFootballMatch);
}
