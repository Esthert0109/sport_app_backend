package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballCompetition;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FootballCompetitionDao extends BasicDao<FootballCompetition> {

    @Insert("insert into football_competition(id,name_zh,name_en,short_name_zh,short_name_en,logo,type,updated_at)values("
            + "#{id},#{nameZh},#{nameEn},#{shortNameZh},#{shortNameEn},#{logo},#{type},#{updatedAt})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    Integer insert(FootballCompetition footballCompetition);

    @Select("select max(id) from football_competition")
    Integer getMaxId();

    @Select("select max(updated_at) from football_competition")
    Integer getMaxUpdatedAt();


    @Update("update football_competition set logo=#{logo},name_zh=#{nameZh},name_en=#{nameEn},short_name_zh=#{shortNameZh},short_name_en=#{shortNameEn},type=#{type},updated_at=#{updatedAt} ")
    void updateDataById(FootballCompetition footballCompetition);

    @Select("select * from football_competition")
    List<FootballCompetition> getAll();

    @Select("select * from football_competition where id=#{competitionId}")
    FootballCompetition getFootballCompetitionById(@Param("competitionId") Integer competitionId);
}
