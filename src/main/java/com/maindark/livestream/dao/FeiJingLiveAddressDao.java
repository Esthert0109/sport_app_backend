package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingLiveAddress;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingLiveAddressDao {

    @Insert("insert into fei_jing_live_address(sport_id, match_id, push_url1, push_url2, push_url3)values" +
            "(#{sportId},#{matchId},#{pushUrl1},#{pushUrl2},#{pushUrl3}) ")
    void insertData(FeiJingLiveAddress feiJingLiveAddress);

    @Select("select count(1) from fei_jing_live_address where sport_id = 1 and match_id=#{matchId} ")
    int queryFootballLiveExisted(@Param("matchId")Integer matchId);
}
