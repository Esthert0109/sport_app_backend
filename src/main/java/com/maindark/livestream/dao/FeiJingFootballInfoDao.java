package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingFootballInfoDao {

    @Select("select count(1) from fei_jing_football_info where record_id=#{recordId}")
    int queryExisted(@Param("recordId") Integer recordId);


    @Insert("insert into fei_jing_football_info(record_id, match_id, league_id, league_name, home_team, away_team, type, title, content, update_time) values( #{recordId}, #{matchId}, #{leagueId}, #{leagueName}, #{homeTeam}, #{awayTeam}, #{type}, #{title}, #{content}, #{updateTime})")
    void insertData(FeiJingInfo feiJingInfo);
}
