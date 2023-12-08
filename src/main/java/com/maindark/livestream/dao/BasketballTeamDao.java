package com.maindark.livestream.dao;

import com.maindark.livestream.domain.BasketballTeam;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BasketballTeamDao {
    @Select("select max(competition_id) from basketball_team")
    Integer getMaxId();

    @Select("select max(updated_at) from basketball_team")
    Integer getMaxUpdatedAt();

    @Insert("insert into basketball_team (team_id, competition_id, conference_id, name_zh, name_en, logo, updated_at) " +
            "values (#{teamId},#{competitionId},#{conferenceId},#{nameZh},#{nameEn},#{logo},#{updatedAt})")
    Integer insertData(BasketballTeam basketballTeam);

    @Update("update basketball_team set competition_id=#{competitionId},name_zh=#{nameZh},name_en=#{nameEn},logo=#{logo},updated_at=#{updatedAt} where team_id=#{teamId}")
    void updateData(BasketballTeam basketballTeam);

    @Select("select * from basketball_team where team_id=#{teamId}")
    BasketballTeam getTeamById(@Param("teamId")Long teamId);

    @Select("select count(1) from basketball_team where team_id =#{teamId}")
    Integer queryExist(@Param("teamId")Long teamId);

    @Select("select * from basketball_team where name_en=#{teamName} limit 1")
    BasketballTeam getTeamByName(@Param("teamName")String teamName);
}
