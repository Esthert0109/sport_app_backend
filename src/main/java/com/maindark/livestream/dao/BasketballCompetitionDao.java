package com.maindark.livestream.dao;

import com.maindark.livestream.domain.BasketballCompetition;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BasketballCompetitionDao {

    @Insert("insert into basketball_competition (competition_id, category_id, name_zh, name_en, logo, type, updated_at) " +
            "VALUES (#{competitionId},#{categoryId},#{nameZh},#{nameEn},#{logo},#{type},#{updatedAt})")
    Integer insertData(BasketballCompetition basketballCompetition);

    @Update("update basketball_competition set name_zh=#{nameZh},name_en=#{nameEn},logo=#{logo},type=#{type},updated_at=#{updatedAt} where competition_id=#{competitionId} ")
    void updateData(BasketballCompetition basketballCompetition);

    @Select("select count(1) from basketball_competition where competition_id=#{competitionId}")
    Integer queryExist(@Param("competitionId")Long competitionId);

    @Select("select max(competition_id) from basketball_competition")
    Integer getMaxId();

    @Select("select max(updated_at) from basketball_competition")
    Integer getMaxUpdatedAt();
}
