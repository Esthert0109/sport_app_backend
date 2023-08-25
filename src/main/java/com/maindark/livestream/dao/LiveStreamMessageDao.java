package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LiveStreamMessageDao {
    @Select("select * from live_stream_message where status = '0' and id=#{id}")
    public LiveStreamMessage getById(@Param("id")Integer id);

}
