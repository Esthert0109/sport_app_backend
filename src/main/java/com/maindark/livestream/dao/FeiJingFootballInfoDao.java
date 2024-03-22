package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingFootballInfor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingFootballInfoDao {

    @Select("select count(1) from fei_jing_football_infor where record_id=#{recordId}")
    int queryExisted(@Param("recordId") Integer recordId);


    @Insert("insert into fei_jing_football_infor(record_id, type, title, content) values( #{recordId},#{type}, #{title}, #{content})")
    void insertData(FeiJingFootballInfor feiJingInfo);
}
