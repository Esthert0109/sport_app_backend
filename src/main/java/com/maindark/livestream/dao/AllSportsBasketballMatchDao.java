package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsBasketballMatch;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AllSportsBasketballMatchDao {
    @Insert("insert into all_sports_basketball_match (match_id, competition_id, competition_name, home_team_id, away_team_id, " +
            "home_team_name, away_team_name, home_team_logo, away_team_logo, status, " +
            "match_time, match_date, home_score, away_score, season) values(" +
            "#{matchId},#{competitionId},#{competitionName},#{homeTeamId},#{awayTeamId}," +
            "#{homeTeamName},#{awayTeamName},#{homeTeamLogo},#{awayTeamLogo},#{status}," +
            "#{matchTime},#{matchDate},#{homeScore},#{awayScore},#{season})")
    Integer insertData(AllSportsBasketballMatch allSportsBasketballMatch);

    @Update("update all_sports_basketball_match set match_time=#{matchTime}, status=#{status},home_score=#{homeScore}," +
            "away_score=#{awayScore},event_live=#{eventLive} where match_id=#{matchId}")
    void updateData(AllSportsBasketballMatch allSportsBasketballMatch);

    @Select("select count(1) from all_sports_basketball_match where match_id=#{matchId}")
    Integer queryExist(@Param("matchId") Long matchId);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where " +
            "competition_name like '%${competitionName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc ,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsBasketMatchByCompetitionName(@Param("competitionName") String competitionName, @Param("from") String from, @Param("to") String to, @Param("pageSize") int pageSize, @Param("offset") long offset);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where " +
            "home_team_name like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsBasketMatchByHomeTeamName(@Param("teamName") String teamName, @Param("from") String from, @Param("to") String to, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where " +
            " away_team_name like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsBasketMatchByAwayTeamName(@Param("teamName") String teamName, @Param("from") String from, @Param("to") String to, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where  " +
            " t.status = '' and t.match_date >= #{nowDate} and t.match_date <#{tomorrowDate} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsStart(@Param("nowDate") String nowDate, @Param("tomorrowDate") String tomorrowDate, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where  " +
            "t.match_date >= #{pastDate} and t.match_date <#{nowDate} and t.status = 'Finished' order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsPast(@Param("pastDate") String pastDate, @Param("nowDate") String nowDate, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where  " +
            "t.status = '' and t.match_date >= #{tomorrowDate} and t.match_date <#{futureDate} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsFuture(@Param("tomorrowDate") String tomorrowDate, @Param("futureDate") String futureDate, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status= '' order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getTodayNotStartMatches(@Param("date") String date, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status= 'Finished' order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getTodayFinishedMatches(@Param("date") String date, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getAllSportsByDate(@Param("date") String date, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t inner join all_sports_collection co on co.match_id = t.match_id where co.user_id=#{userId} order by t.match_date desc,t.match_time asc  ")
    List<BasketballMatchVo> getAllSportsMatchByUserId(@Param("userId") Long userId);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t inner join all_sports_collection co on co.match_id= t.match_id where co.user_id=#{userId} order by t.match_date desc,t.match_time asc limit 3")
    List<BasketballMatchVo> getThreeCollectionsByUserId(@Param("userId") Long userId);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t where t.match_id=#{matchId}")
    BasketballMatchVo getAllSportsBasketMatchByMatchId(@Param("matchId")Long matchId);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from all_sports_basketball_match t  where t.match_id=#{matchId}")
    BasketballMatchVo getBasketballMatchVoByMatchId(@Param("matchId") Long matchId);
}
