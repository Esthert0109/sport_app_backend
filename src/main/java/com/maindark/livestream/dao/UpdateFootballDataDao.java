package com.maindark.livestream.dao;

import com.maindark.livestream.domain.UpdateFootballData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UpdateFootballDataDao {

    @Insert("insert into update_football_data(id, match_id,season_id,competition_id,pub_time, update_time,unique_key)values("
            + "#{id}, #{matchId}, #{seasonId}, #{competitionId}, #{pubTime}, #{updateTime},#{uniqueKey} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    public long insert(UpdateFootballData updateFootballData);

    @Select("select id from update_football_data where unique_key=#{uniqueKey}")
    public UpdateFootballData getDataByUniqueKey(@Param("uniqueKey")Long uniqueKey);

}
