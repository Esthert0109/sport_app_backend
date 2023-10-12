package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsFootballCompetition;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AllSportsFootballCompetitionDao {

    @Insert("insert into all_sports_football_competition(id, competition_name, country, country_logo, logo) values (" +
            "#{id},#{competitionName},#{country},#{countryLogo},#{logo})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=int.class, before=false, statement="select last_insert_id()")
    int insert(AllSportsFootballCompetition allSportsFootballCompetition);

    @Select("select * from all_sports_football_competition where id=#{id}")
    AllSportsFootballCompetition getAllSportsFootballCompetitionById(@Param("id")Integer id);
}
