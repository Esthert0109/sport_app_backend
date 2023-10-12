package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballTeam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AllSportsFootballTeamDao {
    @Insert("insert into all_sports_football_team(id,coach_name, competition_id, team_logo,team_name) values (" +
            "#{id},#{coachName},#{competitionId},#{teamLogo},#{teamName})")
    Integer insert(AllSportsFootballTeam allSportsFootballTeam);

    @Select("select * from all_sports_football_team where id=#{id}")
    AllSportsFootballTeam getAllSportsTeamById(@Param("id")Integer id);
}
