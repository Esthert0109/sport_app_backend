package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballVenue;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballVenueDao {

    @Insert("insert into football_venue(id, name_zh, name_en, capacity, updated_at) VALUES (#{id},#{nameZh},#{nameEn},#{capacity},#{updatedAt})")
    public Integer insert(FootballVenue footballVenue);

    @Update("update football_venue set name_zh=#{nameZh},name_en=#{nameEn},capacity=#{capacity},updated_at=#{updatedAt}")
    public void updateFootballVenue(FootballVenue footballVenue);

    @Select("select name_zh from football_venue where id=#{id}")
    public FootballVenue getFootballVenueById(@Param("id")Integer id);
}
