package com.maindark.livestream.dao;

import com.maindark.livestream.domain.UpdateFootballData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UpdateFootballDataDao {

    @Insert("insert into update_football_data(match_id,season_id,competition_id,pub_time, update_time,unique_key)" +
            "values("
            + "#{matchId}, #{seasonId}, #{competitionId}, #{pubTime}, #{updateTime},#{uniqueKey})")
    Integer insert(UpdateFootballData updateFootballData);

    @Select("select * from update_football_data where unique_key=#{uniqueKey}")
    UpdateFootballData getDataByUniqueKey(@Param("uniqueKey") Long uniqueKey);

}
