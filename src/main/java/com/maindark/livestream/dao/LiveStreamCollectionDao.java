package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamCollection;
import org.apache.ibatis.annotations.*;


@Mapper
public interface LiveStreamCollectionDao {

    @Insert("insert live_stream_collection(user_id,match_id,category,status) values " +
            "(#{userId},#{matchId},#{category},#{status})")
    Integer insert(LiveStreamCollection liveStreamCollection);




    @Delete("delete from live_stream_collection where user_id=#{userId} and match_id=#{matchId}")
    void deleteCollectionById(@Param("userId")Long userId,@Param("matchId") Integer matchId);





}
