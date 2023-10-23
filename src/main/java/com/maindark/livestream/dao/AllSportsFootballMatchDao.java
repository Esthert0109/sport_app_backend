package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballMatch;
import com.maindark.livestream.vo.FootballMatchVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Update("update all_sports_football_match set status=#{status},event_live=#{eventLive},home_team_score=#{homeTeamScore},away_team_score=#{awayTeamScore},home_formation=#{homeFormation},away_formation=#{awayFormation},line_up=#{lineUp} where id = #{id}")
    void updateAllSportsMatch(AllSportsFootballMatch allSportsFootballMatch);

    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where " +
            "competition_name like '%${competitionName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getAllSportsFootMatchByCompetitionName(@Param("competitionName")String competitionName, @Param("from")String from, @Param("to")String to,@Param("pageSize")Integer pageSize,@Param("offset")long offset);
    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where " +
            "home_team_name like '%${teamName}$' or away_team_name like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getAllSportsFootMatchByTeamName(@Param("teamName")String teamName, @Param("from")String from, @Param("to")String to,@Param("pageSize")Integer pageSize,@Param("offset")long offset);
    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where  " +
            "t.match_date >= #{pastDate} and t.match_date <#{nowDate} and t.status = 'Finished' order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getAllSportsPast(@Param("pastDate") String pastDate, @Param("nowDate") String nowDate, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where  " +
            "t.match_date >= #{nowDate} and t.match_date <#{tomorrowDate} order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getAllSportsStart(@Param("nowDate") String nowDate, @Param("tomorrowDate") String tomorrowDate, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where  " +
            "t.match_date >= #{tomorrowDate} and t.match_date <#{futureDate} order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getAllSportsFuture(@Param("tomorrowDate") String tomorrowDate, @Param("futureDate") String futureDate, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date} order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getAllSportsByDate(@Param("date") String date, @Param("pageSize") int pageSize, @Param("offset") long offset);

    @Select("select t.id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation from all_sports_football_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status= '' order by t.id asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getTodayNotStartMatches(String date, int pageSize, long offset);
}
