package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AnchorFollower;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AnchorFollowerDao {
    @Insert("insert into anchor_follow(anchor_id, follower_id, follow_date, cancel_date) values (" +
            "#{anchorId},#{followerId},#{followDate},#{cancelDate})")
    void insertData(AnchorFollower anchorFollower);

    @Delete("delete from anchor_follow where anchor_id=#{anchorId} and follower_id=#{followerId}")
    void deleteData(@Param("anchorId")Long anchorId,@Param("followerId")Long followerId);
}
