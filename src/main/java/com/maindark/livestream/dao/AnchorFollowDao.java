package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AnchorFollow;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface AnchorFollowDao {


    @Insert("insert into anchor_follow(anchor_id, follower_id) values (#{anchorId}, #{followerId})")
    void createFollowAnchor(@Param("anchorId") Long anchorId, @Param("followerId") Long followerId);

    @Update("update anchor_follow set status = #{status} ,follow_updated_time = NOW() where id = #{id}")
    void updateFollowAnchorStatusById(@Param("id") Long id, @Param("status") Boolean status);

    @Select("SELECT EXISTS(SELECT 1 FROM anchor_follow where anchor_id = #{anchorId} and follower_id = #{followerId})")
    boolean checkFollowExistByAnchorIdFollowerId(@Param("anchorId") Long anchorId, @Param("followerId") Long followerId);

    @Select("Select * from anchor_follow where anchor_id = #{anchorId} and follower_id = #{followerId}")
    AnchorFollow getFollowDetailsByAnchorIdFollowerId(@Param("anchorId") Long anchorId, @Param("followerId") Long followerId);
}
