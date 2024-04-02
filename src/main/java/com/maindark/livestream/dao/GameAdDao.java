package com.maindark.livestream.dao;

import com.maindark.livestream.vo.GameAdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GameAdDao {

    @Select("select ad_url from ad_addr limit 1")
    GameAdVo getGameAd();
}
