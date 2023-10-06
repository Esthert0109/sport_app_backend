package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballReferee;
import com.maindark.livestream.domain.FootballVenue;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballRefereeDao {
    @Insert("insert into football_referee(id, name_zh, name_en, birthday, age, updated_at) VALUES (#{id},#{nameZh},#{nameEn},#{birthday},#{age},#{updatedAt})")
    public Integer insert(FootballReferee footballReferee);

    @Update("update football_referee set name_zh=#{nameZh},name_en=#{nameEn},birthday=#{birthday},age=#{age},updated_at=#{updatedAt} where id=#{id}")
    public void updateFootballReferee(FootballReferee footballReferee);

    @Select("select name_zh from football_referee where id=#{id}")
    public FootballReferee getFootballRefereeById(@Param("id")Integer id);

    @Select("select max(id) from football_referee")
    public Integer getMaxId();

    @Select("select max(updated_at) from football_referee")
    public Integer getMaxUpdatedAt();

}
