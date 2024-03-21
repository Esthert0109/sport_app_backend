package com.maindark.livestream.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingFootballAnimationDao {
    @Select("select count(1) from fei_jing_football_animation where match_id=#{matchId}")
    int queryExisted(@Param("matchId")Integer matchId);

    @Insert("insert into fei_jing_football_animation(match_id)values(#{matchId}) ")
    void insertData(@Param("matchId")Integer matchId);
}
