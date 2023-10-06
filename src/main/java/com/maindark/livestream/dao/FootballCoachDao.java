package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballCoach;
import com.maindark.livestream.domain.FootballTeam;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FootballCoachDao {

    @Insert("insert into football_coach(id, name_zh, name_en, logo, preferred_formation, team_id, updated_at)values("
            + "#{id},#{nameZh}, #{nameEn}, #{logo},#{preferredFormation},#{teamId}, #{updatedAt})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=Integer.class, before=false, statement="select last_insert_id()")
    public Integer insert(FootballCoach footballCoach);

    @Select("select max(id) from football_coach")
    public Integer getMaxId();

    @Select("select max(updated_at) from football_coach")
    public Integer getMaxUpdatedAt();


    @Update("update football_coach set logo=#{logo},name_zh=#{nameZh},name_en=#{nameEn},preferred_formation=#{perferredFormation},updated_at=#{updatedAt} where id=${id}")
    public void updateDataById(FootballCoach footballCoach);


}
