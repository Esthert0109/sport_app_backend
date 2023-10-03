package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballTeam;
import org.apache.ibatis.annotations.*;


@Mapper
public interface FootballTeamDao extends BasicDao<FootballTeam> {



    @Insert("insert into football_team(id, competition_id,name_zh,name_en,logo,updated_at)values("
            + "#{id}, #{competitionId}, #{nameZh}, #{nameEn}, #{logo}, #{updatedAt})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    public Integer insert(FootballTeam footBallTeam);

    @Select("select max(id) from football_team")
    public Integer getMaxId();

    @Select("select max(updated_at) from football_team")
    public Integer getMaxUpdatedAt();


    @Update("update football_team set logo=#{logo},name_zh=#{nameZh},name_en=#{nameEn},updated_at=#{updatedAt}")
    public void updateDataById(FootballTeam footballTeam);

    @Select("select * from football_team where id=#{id}")
    FootballTeam getTeam(@Param("id")Integer id);
}
