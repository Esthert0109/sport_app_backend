package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AnchorFollow;
import com.maindark.livestream.vo.AnchorFollowVo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface AnchorFollowDao {


    @Insert("insert into follow(anchor_id, follower_id) values (#{anchorId}, #{followerId})")
    void createFollowAnchor(@Param("anchorId") Long anchorId, @Param("followerId") Long followerId);

    @Update("update follow set status = #{status} ,follow_updated_time = NOW() where id = #{id}")
    void updateFollowAnchorStatusById(@Param("id") Long id, @Param("status") Boolean status);

    @Select("SELECT EXISTS(SELECT 1 FROM follow where anchor_id = #{anchorId} and follower_id = #{followerId})")
    boolean checkFollowExistByAnchorIdFollowerId(@Param("anchorId") Long anchorId, @Param("followerId") Long followerId);

    @Select("Select * from follow where anchor_id = #{anchorId} and follower_id = #{followerId}")
    AnchorFollow getFollowDetailsByAnchorIdFollowerId(@Param("anchorId") Long anchorId, @Param("followerId") Long followerId);

    @Select("SELECT * FROM follow WHERE follower_id = #{followerId} AND status = true ORDER BY streaming_status DESC, follow_updated_time DESC")
    List<AnchorFollowVo> getFollowingListByFollowerId(@Param("followerId") Long followerId);
}
