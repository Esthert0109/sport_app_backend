package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LiveStreamDetailDao {

    @Insert("insert into live_stream_detail(user_id, cover, push_host, push_code, live_date,is_popular) values(#{userId},#{cover},#{pushHost},#{pushCode},#{liveDate},#{isPopular}) ")
   Integer insertData(LiveStreamDetail liveStreamDetail);
}
