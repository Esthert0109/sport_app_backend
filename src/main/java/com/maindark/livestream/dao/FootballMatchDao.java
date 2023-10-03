package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballMatch;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballMatchDao extends BasicDao<FootballMatch> {



    @Insert("insert into football_match(id,season_id,competition_id,status_id,match_time,home_team_id,away_team_id,home_team_name,away_team_name,home_team_score,away_team_score,line_up,updated_at)values("
            + "#{id},#{seasonId},#{competitionId},#{status},#{matchTime},#{homeTeamId},#{awayTeamId},#{homeTeamName},#{awayTeamName},#{homeTeamScore},#{awayTeamScore},#{lineUp},#{updatedAt})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    public Integer insert(FootballMatch footballMatch);

    @Select("select max(id) from football_match")
    Integer getMaxId();

    @Select("select max(updated_at) from football_match")
    Integer getMaxUpdatedAt();

    @Update("update football_match set season_id=#{seasonId},competition_id=#{competitionId},status_id=#{status},match_time=#{matchTime},home_team_score=#{homeTeamScore},away_team_score=#{awayTeamScore},updated_at=#{updatedAt} where id=#{id}")
    public void updateDataById(FootballMatch footballMatch);

    @Select("select line_up from football_match where id=#{id}")
    public FootballMatch getFootballMatch(@Param("id")Integer id);

    @Select("select * from football_match where id=#{id}")
    public FootballMatch getFootballMatchById(@Param("id")Integer id);

}
