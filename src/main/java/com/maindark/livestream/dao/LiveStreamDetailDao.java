package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamDetail;
import com.maindark.livestream.vo.LiveStreamDetailVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LiveStreamDetailDao {

    @Insert("insert into live_stream_detail(user_id, sport_type, cover,title, push_host, push_code, live_date,is_popular) values(#{userId},#{sportType},#{cover},#{title},#{pushHost},#{pushCode},#{liveDate},#{isPopular}) ")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=int.class, before=false, statement="select last_insert_id()")
    Integer insertData(LiveStreamDetail liveStreamDetail);

    @Select("select id, user_id,sport_type, cover, title, push_host, push_code, date_format(live_date,'%Y-%m-%d %H:%i:%s') as live_date, is_popular from live_stream_detail where id=#{id}")
    LiveStreamDetailVo getLiveStreamDetailById(@Param("id")Integer id);
    @Update("update live_stream_detail set title=#{title},cover=#{cover} where id=#{id}")
    void updateLiveStreamDetailById(LiveStreamDetail liveStreamDetail);

    @Select("select id, user_id,sport_type, cover, title, push_host, push_code, date_format(live_date,'%Y-%m-%d %H:%i:%s') as live_date, is_popular from live_stream_detail where is_popular= '1' order by live_date desc limit #{pageSize} offset #{offset}")
    List<LiveStreamDetailVo> getAllPopularLiveStreamDetails(@Param("pageSize")int pageSize,@Param("offset")long offset);
    @Select("select id, user_id,sport_type, cover, title, push_host, push_code, date_format(live_date,'%Y-%m-%d %H:%i:%s') as live_date, is_popular from live_stream_detail order by live_date desc limit #{pageSize} offset #{offset}")
    List<LiveStreamDetailVo> getAllLiveStreamDetails(@Param("pageSize")int pageSize,@Param("offset")long offset);
}
