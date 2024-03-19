package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingBasketballMatch;
import com.maindark.livestream.vo.BasketballMatchVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
            "#{matchDate},#{hFQuarter},#{hSQuarter},#{hTQuarter},#{h4Quarter}," +
            "#{aFQuarter},#{aSQuarter},#{aTQuarter},#{a4Quarter},#{homeScore},#{awayScore},#{homeCoach}," +
            "#{awayCoach},#{hasState},#{season})")
    void insertData(FeiJingBasketballMatch feiJingBasketballMatch);
    @Select("select count(1) from fei_jing_basketball_match where match_id=#{matchId}")
    int queryExisted(@Param("matchId") Integer matchId);

    void updateMatchScoreByMatchId(FeiJingBasketballMatch feiJingBasketballMatch);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.home_team_id,t.away_team_id,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where " +
            "competition_name like '%${competitionName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc ,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingBasketMatchByCompetitionName(@Param("competitionName") String competitionName, @Param("from") String from, @Param("to") String to, @Param("pageSize") int pageSize, @Param("offset") long offset);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where " +
            "home_team_name like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingBasketMatchByHomeTeamName(@Param("teamName") String teamName, @Param("from") String from, @Param("to") String to, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where " +
            " away_team_name like '%${teamName}%' and t.match_date >= #{from} and t.match_date <#{to} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingBasketMatchByAwayTeamName(@Param("teamName") String teamName, @Param("from") String from, @Param("to") String to, @Param("pageSize") int pageSize, @Param("offset") long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where  " +
            " t.status > 1 and t.match_date >= #{nowDate} and t.match_date <#{tomorrowDate} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingStart(@Param("nowDate") String nowDate, @Param("tomorrowDate") String tomorrowDate, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where  " +
            "t.match_date >= #{pastDate} and t.match_date <#{nowDate} and t.status = '-1' order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingPast(@Param("pastDate") String pastDate, @Param("nowDate") String nowDate, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where  " +
            "t.status = '0' and t.match_date >= #{tomorrowDate} and t.match_date <#{futureDate} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingFuture(@Param("tomorrowDate") String tomorrowDate, @Param("futureDate") String futureDate, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status= '0' order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getTodayNotStartMatches(@Param("date") String date, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date}  and t.status= '-1' order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getTodayFinishedMatches(@Param("date") String date, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where  " +
            "t.match_date >= #{date} and t.match_date <=#{date} order by t.match_date desc,t.match_time asc limit #{pageSize} offset #{offset}")
    List<BasketballMatchVo> getFeiJingMatchByDate(@Param("date") String date, @Param("pageSize") Integer pageSize, @Param("offset") Long offset);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr," +
            "t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name," +
            "t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score," +
            "t.away_score as away_team_score from fei_jing_basketball_match t inner join live_stream_collection co on co.match_id = t.match_id " +
            "where co.user_id=#{userId} order by t.match_date desc,t.match_time desc  ")
    List<BasketballMatchVo> getFeiJingMatchByUserId(@Param("userId") Long userId,@Param("pageSize") Integer pageSize, @Param("offset") Long offset);

    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr," +
            "t.status as statusStr,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo," +
            "t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score " +
            "from fei_jing_basketball_match t inner join live_stream_collection co on co.match_id= t.match_id " +
            "where co.user_id=#{userId} order by t.match_date desc,t.match_time desc limit 3")
    List<BasketballMatchVo> getThreeCollectionsByUserId(@Param("userId") Long userId);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t where t.match_id=#{matchId}")
    BasketballMatchVo getFeiJingBasketMatchByMatchId(@Param("matchId")Integer matchId);
    @Select("select t.match_id as id,t.competition_name,t.match_time as matchTimeStr,t.status,t.match_date,t.home_team_name,t.away_team_name,t.home_team_logo,t.away_team_logo,t.status,t.home_score as home_team_score,t.away_score as away_team_score from fei_jing_basketball_match t  where t.match_id=#{matchId}")
    BasketballMatchVo getBasketballMatchVoByMatchId(@Param("matchId") Integer matchId);
}
