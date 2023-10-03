package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.vo.LiveStreamCollectionVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LiveStreamCollectionDao {

    @Insert("insert live_stream_collection(user_id,match_id,category,status) values (#{userId},#{matchId},#{category},#{status})")
    public Integer insert(LiveStreamCollection liveStreamCollection);

    @Select("select * from live_stream_collection where user_id=#{userId}")
    public List<LiveStreamCollectionVo> getAllCollections(@Param("userId")Long userId);
    @Select("select * from live_stream_collection where match_id=#{id}")
    public LiveStreamCollectionVo getCollectionByMatchId(@Param("id")Integer id);

    @Delete("update live_stream_collection set status='0' where id=#{id}")
    public int deleteCollectionById(@Param("id")Integer id);


}
