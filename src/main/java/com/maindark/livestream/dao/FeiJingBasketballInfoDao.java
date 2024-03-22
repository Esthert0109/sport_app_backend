package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJIngBasketballInfor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingBasketballInfoDao {

    @Select("select count(1) from fei_jing_basketball_infor where record_id=#{recordId}")
    int queryExisted(@Param("recordId") Integer recordId);


    @Insert("insert into fei_jing_basketball_infor(record_id, type, title, content) values( #{recordId}, #{type}, #{title}, #{content})")
    void insertData(FeiJIngBasketballInfor feiJingInfo);
}
