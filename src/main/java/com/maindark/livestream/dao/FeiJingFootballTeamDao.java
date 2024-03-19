package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingFootballTeam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingFootballTeamDao {

    @Insert("insert into fei_jing_football_team (team_id, league_id, logo, name_en, name_cn, coach_cn, coach_en, coach_id) values(" +
            "#{teamId},#{leagueId},#{logo},#{nameEn},#{nameCn},#{coachCn},#{coachEn},#{coachId})")
    void insertData(FeiJingFootballTeam feiJingFootballTeam);

    @Select("select count(1) from fei_jing_football_team where team_id=#{teamId}")
    int queryExisted(@Param("teamId")Integer teamId);

    @Select("select logo,coach_cn from fei_jing_football_team where team_id=#{teamId}")
    FeiJingFootballTeam getTeamLogoByTeamId(@Param("teamId")Integer teamId);


}
