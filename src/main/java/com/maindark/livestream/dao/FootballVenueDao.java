package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballVenue;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballVenueDao {

    @Insert("insert into football_venue(id, name_zh, name_en, capacity, updated_at) VALUES (#{id},#{nameZh},#{nameEn},#{capacity},#{updatedAt})")
    public Integer insert(FootballVenue footballVenue);

    @Update("update football_venue set name_zh=#{nameZh},name_en=#{nameEn},capacity=#{capacity},updated_at=#{updatedAt} where id=#{id}")
    public void updateFootballVenue(FootballVenue footballVenue);

    @Select("select name_zh from football_venue where id=#{id}")
    public FootballVenue getFootballVenueById(@Param("id")Integer id);

    @Select("select max(id) from football_venue")
    public Integer getMaxId();

    @Select("select max(updated_at) from football_venue")
    public Integer getMaxUpdatedAt();
}
