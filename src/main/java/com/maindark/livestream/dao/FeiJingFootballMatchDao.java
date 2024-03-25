package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.vo.FootballMatchVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where " +
            "t.league_cns_short like '%${competitionName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc ,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingFootMatchByCompetitionName(@Param("competitionName")String competitionName, @Param("from")String from, @Param("to")String to,@Param("pageSize")Integer pageSize,@Param("offset")long offset);
    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where " +
            "t.home_team_name_cn like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingFootMatchByHomeTeamName(@Param("teamName")String teamName, @Param("from")String from, @Param("to")String to,@Param("pageSize")Integer pageSize,@Param("offset")long offset);

    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where " +
            "t.away_team_name_cn  like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingFootMatchByAwayTeamName(@Param("teamName")String teamName, @Param("from")String from, @Param("to")String to,@Param("pageSize")Integer pageSize,@Param("offset")long offset);

    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where  " +
            "t.match_date >= #{pastDate} and t.match_date <#{nowDate} and t.status_id = -1 order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingPast(@Param("pastDate") String pastDate, @Param("nowDate") String nowDate, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where  " +
            "t.status_id = 0 and t.match_date >= #{nowDate} and t.match_date <#{tomorrowDate} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingStart(@Param("nowDate") String nowDate, @Param("tomorrowDate") String tomorrowDate, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where  " +
            "t.status_id = 0 and t.match_date >= #{tomorrowDate} and t.match_date <#{futureDate} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingFuture(@Param("tomorrowDate") String tomorrowDate, @Param("futureDate") String futureDate, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getFeiJingByDate(@Param("date") String date, @Param("pageSize") int pageSize, @Param("offset") long offset);

    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status_id= 0 order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getTodayNotStartMatches(String date, int pageSize, long offset);
    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status_id= -1 order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<FootballMatchVo> getTodayFinishedMatches(String date, int pageSize, long offset);

    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name," +
            "t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up," +
            "t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t inner join live_stream_collection co on co.match_id = t.match_id where " +
            "co.user_id=#{userId} order by t.match_date desc,t.match_time desc limit #{limit} offset #{offset}  ")
    List<FootballMatchVo> getFeiJingMatchByUserId(@Param("userId") Long userId,@Param("limit")int limit,@Param("offset")long offset);

    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name," +
            "t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score," +
            "t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t inner join live_stream_collection co on co.match_id= t.match_id " +
            "where co.user_id=#{userId} order by t.match_date desc,t.match_time desc limit 3")
    List<FootballMatchVo> getThreeCollectionsByUserId(@Param("userId") Long userId);
    @Select("select t.match_id as id,t.league_cns_short as competition_name,t.match_time as matchTimeStr,t.match_date,t.home_team_name_cn as home_team_name,t.away_team_name_cn as away_team_name,t.home_team_logo,t.away_team_logo,t.status_id,t.home_team_score,t.away_team_score,t.line_up,t.home_formation,t.away_formation,t.venue as venueName from fei_jing_football_match t where t.match_id=#{matchId}")
    FootballMatchVo getFeiJingFootballMatchByMatchId(@Param("matchId")Integer matchId);
    @Select("select match_id from fei_jing_football_match where home_team_id=#{homeTeamId} and away_team_id=#{awayTeamId}  order by match_time asc limit 1")
    FeiJingFootballMatch getFootballMatchByHomeTeamIdAndAwayTeamId(@Param("homeTeamId") Integer homeTeamId, @Param("awayTeamId") Integer awayTeamId);
}
