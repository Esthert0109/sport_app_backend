package com.maindark.livestream.dao;

import com.maindark.livestream.vo.LiveStreamCollectionVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LiveStreamCollectionDao {

    @Select("select * from live_stream_collection where user_id=#{userId}")
    public List<LiveStreamCollectionVo> getAllCollection(@Param("userId")Integer userId);
    @Select("select * from live_stream_collection where competition_id=#{id}")
    public LiveStreamCollectionVo getCollectionByCompetitionId(@Param("id")Integer id);

    @Delete("delete from live_stream_collection where id=#{id}")
    public int deleteCollectionById(@Param("id")Integer id);
}
