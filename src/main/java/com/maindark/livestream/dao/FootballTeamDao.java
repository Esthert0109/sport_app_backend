package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballTeam;
import com.maindark.livestream.vo.FootballTeamVo;
import org.apache.ibatis.annotations.*;


@Mapper
public interface FootballTeamDao extends BasicDao<FootballTeam> {



    @Insert("insert into football_team(id, competition_id,name_zh,name_en,logo,updated_at)values("
            + "#{id}, #{competitionId}, #{nameZh}, #{nameEn}, #{logo}, #{updatedAt})")
    Integer insert(FootballTeam footBallTeam);

    @Select("select max(id) from football_team")
    Integer getMaxId();

    @Select("select max(updated_at) from football_team")
    Integer getMaxUpdatedAt();


    @Update("update football_team set logo=#{logo},name_zh=#{nameZh},name_en=#{nameEn},updated_at=#{updatedAt} where id=#{id}")
    void updateDataById(FootballTeam footballTeam);

    @Select("select * from football_team where id=#{id}")
    FootballTeam getTeam(@Param("id")Integer id);

    @Select("select * from football_team where name_en=#{teamName} limit 1")
    FootballTeamVo getTeamByName(@Param("teamName")String teamName);
}
