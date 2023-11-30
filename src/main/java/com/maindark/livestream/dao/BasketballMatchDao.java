package com.maindark.livestream.dao;

import com.maindark.livestream.domain.BasketballMatch;
import com.maindark.livestream.vo.BasketballMatchVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BasketballMatchDao {
    @Select("select max(match_id) from basketball_match")
    Integer getMaxId();

    @Select("select max(updated_at) from basketball_match")
    Integer getMaxUpdatedAt();

    @Insert("insert into basketball_match(match_id, competition_id, home_team_id, away_team_id," +
            "home_team_name,away_team_name,home_team_logo,away_team_logo, kind, " +
            "period_count, status_id, match_time, " +
            "home_score, away_score, season_id, updated_at) " +
            "VALUES(#{matchId},#{competitionId},#{homeTeamId},#{awayTeamId}," +
            "#{homeTeamName},#{awayTeamName},#{homeTeamLogo},#{awayTeamLogo},#{kind}," +
            "#{periodCount},#{statusId},#{matchTime},#{homeScore},#{awayScore},#{seasonId},#{updatedAt}) ")
    Integer insertData(BasketballMatch basketballMatch);

    @Update("update basketball_match set competition_id=#{competitionId},home_team_id=#{homeTeamId},away_team_id=#{awayTeamId}," +
            "home_team_name=#{homeTeamName},away_team_name=#{awayTeamName},home_team_logo=#{homeTeamLogo},away_team_logo=#{awayTeamLogo}," +
            "kind=#{kind},period_count=#{periodCount}," +
            "status_id=#{statusId},match_time=#{matchTime},home_score=#{homeScore},away_score=#{awayScore}," +
            "season_id=#{seasonId},updated_at=#{updatedAt} where match_id=#{matchId}")
    void updateData(BasketballMatch basketballMatch);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where c.name_zh like '%${competitionName}%' and b.match_time >=#{nowSeconds} " +
            "and b.match_time < #{tomorrowSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchByCompetitionName(@Param("competitionName") String competitionName, @Param("nowSeconds") long nowSeconds, @Param("tomorrowSeconds") long tomorrowSeconds, @Param("limit") Integer limit, @Param("offset") long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.home_team_name like '%${teamName}%' and b.match_time >=#{nowSeconds} " +
            "and b.match_time < #{tomorrowSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchByHomeTeamName(@Param("teamName") String teamName, @Param("nowSeconds") long nowSeconds, @Param("tomorrowSeconds") long tomorrowSeconds, @Param("limit") Integer limit, @Param("offset") long offset);
    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.away_team_name like '%${teamName}%' and b.match_time >=#{nowSeconds} " +
            "and b.match_time < #{tomorrowSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchByAwayTeamName(@Param("teamName") String teamName, @Param("nowSeconds") long nowSeconds, @Param("tomorrowSeconds") long tomorrowSeconds, @Param("limit") Integer limit, @Param("offset") long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.status_id > 1 and b.status_id <= 10 and b.match_time >=#{nowSeconds} " +
            "and b.match_time < #{tomorrowSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchesStart(@Param("nowSeconds") Long nowSeconds, @Param("tomorrowSeconds") Long tomorrowSeconds, @Param("limit") Integer limit, @Param("offset") Long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.status_id = 10 and b.match_time >=#{pastSeconds} " +
            "and b.match_time < #{nowSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchesPast(@Param("pastSeconds") Long pastSeconds, @Param("nowSeconds") Long nowSeconds, @Param("limit") Integer limit, @Param("offset") Long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.status_id = 1 and b.match_time >=#{tomorrowSeconds} " +
            "and b.match_time < #{futureSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchesFuture(@Param("tomorrowSeconds") Long tomorrowSeconds, @Param("futureSeconds") Long futureSeconds,  @Param("limit") Integer limit, @Param("offset") long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.status_id = 1 and b.match_time >=#{nowSeconds} " +
            "and b.match_time < #{tomorrowSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchNotStart(@Param("nowSeconds") Long nowSeconds, @Param("tomorrowSeconds") Long tomorrowSeconds, @Param("limit") Integer limit, @Param("offset") long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where  b.match_time >=#{currentSeconds} " +
            "and b.match_time < #{deadlineSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchByDate(Long currentSeconds, Long deadlineSeconds, Integer limit, long offset);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.status_id = 10 and b.match_time >=#{currentSeconds} " +
            "and b.match_time < #{deadlineSeconds} order by b.match_time asc limit #{limit} offset #{offset} ")
    List<BasketballMatchVo> getBasketballMatchFinished(Long currentSeconds, Long deadlineSeconds, Integer limit, long offset);

    @Select("select count(1) from basketball_match where match_id=#{matchId}")
    int queryExist(@Param("matchId") Long matchId);

    @Select("select match_time,home_team_name,away_team_name,home_team_logo,away_team_logo from basketball_match where match_id=#{matchId}")
    BasketballMatchVo getBasketBallMatchById(@Param("matchId") Long matchId);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id where b.match_id = #{matchId}")
    BasketballMatchVo getBasketballVoByMatchId(@Param("matchId")Long matchId);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id left join live_stream_collection co on b.match_id= co.match_id where co.user_id = #{userId}")
    List<BasketballMatchVo> getAllBasketballCollectionsByUserId(@Param("userId") Long userId);

    @Select("select b.match_id as id,b.competition_id,b.home_team_name,b.away_team_name,b.home_team_logo,b.away_team_logo," +
            "b.match_time,b.status_id,b.home_score as home_team_score,b.away_score as away_team_score,c.name_zh as competition_name from" +
            " basketball_match b left join basketball_competition c on b.competition_id = c.competition_id left join live_stream_collection co on b.match_id= co.match_id where co.user_id = #{userId} limit 3")
    List<BasketballMatchVo> getThreeBasketballCollectionsByUserId(@Param("userId") Long userId);
}
