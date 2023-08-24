package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LiveStreamUserDao {
    @Select("select * from live_stream_user where id=#{id}")
    public LiveStreamUser getById(@Param("id") long id);

    @Update("update live_stream_user set password = #{password} where id = #{id}")
    public void update(LiveStreamUser toBeUpdate);


    @Insert("insert into live_stream_user(id, nickname, password,salt, head, area_code, register_date)values("
            + "#{id}, #{nickName}, #{password}, #{salt}, #{head}, #{areaCode},#{registerDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    public long insert(LiveStreamUser liveStreamUser);
    @Update("update live_stream_user set nickname = #{nickName} where id=#{id}")
    public void updateNickName(LiveStreamUser user);

    @Update("update live_stream_user set head=#{head} where id=#{id}")
    public void updateHead(LiveStreamUser user);

}
