package com.maindark.livestream.dao;

import com.maindark.livestream.domain.ApplyForLive;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplyForLiveDao {
    @Insert("insert into apply_for_live(user_id, apply_date,status) values (#{userId},#{applyDate},#{status})")
    void insert(ApplyForLive applyForLive);
}
