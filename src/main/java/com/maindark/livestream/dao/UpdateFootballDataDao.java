package com.maindark.livestream.dao;

import com.maindark.livestream.domain.UpdateFootballData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UpdateFootballDataDao {

    @Insert("insert into update_football_data(match_id,season_id,competition_id,pub_time, update_time,unique_key)values("
            + "#{matchId}, #{seasonId}, #{competitionId}, #{pubTime}, #{updateTime},#{uniqueKey})")
    public Integer insert(UpdateFootballData updateFootballData);

    @Select("select id from update_football_data where unique_key=#{uniqueKey}")
    public UpdateFootballData getDataByUniqueKey(@Param("uniqueKey")Long uniqueKey);

}
