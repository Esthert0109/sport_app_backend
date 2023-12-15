package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.vo.LiveStreamUserVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LiveStreamUserDao {
    @Select("select * from live_stream_user where id=#{id}")
    LiveStreamUser getById(@Param("id") long id);

    @Update("update live_stream_user set password = #{password} where id = #{id}")
    void update(LiveStreamUser toBeUpdate);


    @Insert("insert into live_stream_user(id, nickname, password,salt, head, area_code, register_date,role)values("
            + "#{id}, #{nickName}, #{password}, #{salt}, #{head}, #{areaCode},#{registerDate},#{role})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(LiveStreamUser liveStreamUser);
    @Update("update live_stream_user set nickname = #{nickName} where id=#{id}")
    void updateNickName(LiveStreamUser user);

    @Update("update live_stream_user set head=#{head} where id=#{id}")
    void updateHead(LiveStreamUser user);
    @Select("select id,nickname,head,popular_anchor from live_stream_user where popular_anchor = '1'")
    List<LiveStreamUserVo> getPopularAnchors();

}
