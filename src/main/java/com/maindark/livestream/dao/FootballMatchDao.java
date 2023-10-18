package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.vo.FootballMatchVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FootballMatchDao extends BasicDao<FootballMatch> {



    @Insert("insert into football_match(id,season_id,competition_id,status_id,match_time," +
            "home_team_id,away_team_id,referee_id,venue_id,home_team_name,away_team_name,home_team_logo,away_team_logo," +
            "home_team_score,away_team_score,line_up,updated_at)values("
            + "#{id},#{seasonId},#{competitionId},#{statusId},#{matchTime},#{homeTeamId}," +
            "#{awayTeamId},#{refereeId},#{venueId},#{homeTeamName},#{awayTeamName},#{homeTeamLogo},#{awayTeamLogo}" +
            "#{homeTeamScore},#{awayTeamScore},#{lineUp},#{updatedAt})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    Integer insert(FootballMatch footballMatch);

    @Select("select max(id) from football_match")
    Integer getMaxId();

    @Select("select max(updated_at) from football_match")
    Integer getMaxUpdatedAt();

    @Update("update football_match set season_id=#{seasonId},competition_id=#{competitionId}," +
            "status_id=#{statusId},match_time=#{matchTime},home_team_score=#{homeTeamScore}," +
            "away_team_score=#{awayTeamScore},updated_at=#{updatedAt} where id=#{id}")
    void updateDataById(FootballMatch footballMatch);

    @Select("select * from football_match where id=#{id}")
    FootballMatch getFootballMatch(@Param("id") Integer id);

    @Select("select * from football_match where id=#{id}")
    FootballMatch getFootballMatchById(@Param("id") Integer id);

    @Select("select t1.id,t1.competition_id,t1.home_team_id,t1.away_team_id,t1.home_team_name," +
            "t1.away_team_name,t1.home_team_logo,t1.away_team_logo,t1.home_team_score,t1.away_team_score," +
            "t1.match_time,fc.name_zh as competitionName,t1.status_id,t1.line_up " +
            "from live_stream.football_match t1 left join live_stream.football_competition fc " +
            "on t1.competition_id = fc.id where t1.status_id = 8 and t1.id=#{id}")
    FootballMatchVo getFootballMatchVoById(@Param("id") Integer id);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id from live_stream.football_match t1 left join live_stream.football_competition fc on " +
            "t1.competition_id = fc.id where fc.name_zh like'%${competitionName}%'  a" +
            "nd t1.status_id in(2,3,4,5) and t1.match_time >=#{nowSeconds} and t1.match_time<#{tomorrowSeconds} " +
            "order by t1.match_time asc limit #{limit} offset #{offset}")
    List<FootballMatchVo> getFootballMatchByCompetitionName(@Param("competitionName") String competitionName, @Param("nowSeconds")Long nowSeconds, @Param("tomorrowSeconds")Long tomorrowSeconds,@Param("limit")Integer limit,@Param("offset")long offset);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id from football_match t1 left join live_stream.football_competition fc on t1.competition_id = fc.id " +
            "where t1.home_team_name like '%${teamName}%' or t1.away_team_name like'%${teamName}%' " +
            "and t1.status_id in(2,3,4,5) and t1.match_time >#{nowSeconds} and t1.match_time<#{tomorrowSeconds} order by t1.match_time asc")
    List<FootballMatchVo> getFootballMatchByTeamName(@Param("teamName") String teamName,@Param("nowSeconds")Long nowSeconds,@Param("tomorrowSeconds")Long tomorrowSeconds,@Param("limit")Integer limit,@Param("offset")long offset);

    @Update("update football_match set home_formation=#{homeFormation},away_formation=#{awayFormation} " +
            "where id=#{matchId}")
    void updateFormation(@Param("homeFormation") String homeFormation, @Param("awayFormation") String awayFormation,@Param("matchId")Integer matchId);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id,t1.line_up from live_stream.football_match t1 left join live_stream.football_competition " +
            "fc on t1.competition_id = fc.id where t1.status_id in(2,3,4,5) and t1.match_time >=#{nowSeconds} and " +
            "t1.match_time<#{tomorrowSeconds} order by t1.match_time asc")
    List<FootballMatchVo> getFootballMatchByTime(@Param("nowSeconds")Long nowSeconds, @Param("tomorrowSeconds")Long tomorrowSeconds);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id,t1.line_up from live_stream.football_match t1 left join live_stream.football_competition fc" +
            " on t1.competition_id = fc.id where t1.status_id = 8 and t1.match_time >=#{pastSeconds} " +
            "and t1.match_time<#{nowSeconds} order by t1.match_time asc limit #{limit} offset #{offset}")
    List<FootballMatchVo> getFootballMatchesPast(@Param("pastSeconds")Long pastSeconds, @Param("nowSeconds")Long nowSeconds,@Param("limit")Integer limit,@Param("offset")long offset);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id,t1.line_up from live_stream.football_match t1 left join live_stream.football_competition fc" +
            " on t1.competition_id = fc.id where t1.status_id >= 1 and t1.status_id <=5 and t1.match_time >=#{nowSeconds} " +
            "and t1.match_time<#{tomorrowSeconds} order by t1.match_time asc limit #{limit} offset #{offset}")
    List<FootballMatchVo> getFootballMatchesStart(@Param("nowSeconds")Long nowSeconds, @Param("tomorrowSeconds")Long tomorrowSeconds,@Param("limit")Integer limit,@Param("offset")long offset);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id,t1.line_up from live_stream.football_match t1 left join live_stream.football_competition fc" +
            " on t1.competition_id = fc.id where t1.status_id = 1 and t1.match_time >=#{tomorrowSeconds} " +
            "and t1.match_time<#{futureSeconds} order by t1.match_time asc limit #{limit} offset #{offset}")
    List<FootballMatchVo> getFootballMatchesFuture(@Param("tomorrowSeconds")Long tomorrowSeconds, @Param("futureSeconds")Long futureSeconds,@Param("limit")Integer limit,@Param("offset")long offset);

    @Select("select t1.id,t1.competition_id,t1.home_team_name,t1.away_team_name,t1.home_team_logo," +
            "t1.away_team_logo,t1.home_team_score,t1.away_team_score,t1.match_time,fc.name_zh as competitionName," +
            "t1.status_id,t1.line_up from live_stream.football_match t1 left join live_stream.football_competition fc" +
            " on t1.competition_id = fc.id where t1.match_time >=#{currentSeconds} and " +
            "t1.match_time<#{deadlineSeconds} order by t1.match_time asc limit #{limit} offset #{offset}")
    List<FootballMatchVo> getFootballMatchByDate(@Param("currentSeconds") Long currentSeconds, @Param("deadlineSeconds") Long deadlineSeconds,@Param("limit")Integer limit,@Param("offset")long offset);

    @Select("select * from football_match where home_team_id=#{teamId} limit 1")
    FootballMatch getFootballMatchByHomeTeamId(@Param("teamId") Integer teamId);
}
