package com.maindark.livestream.dao;

import com.maindark.livestream.domain.FootballLiveVideo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FootballLiveVideoDao {

    @Insert("insert into football_live_video(match_id, type, title, mobile_link, pc_link) values(#{matchId},#{type},#{title},#{mobileLink},#{pcLink})")
    Integer insert(FootballLiveVideo footballLiveVideo);

    @Update("update football_live_video set type=#{type},title=#{title},mobile_link=#{mobileLink},pc_link=#{pcLink} where match_id=#{matchId}")
    void updateFootballLiveVideoByMatchId(@Param("matchId") Integer matchId);
}
