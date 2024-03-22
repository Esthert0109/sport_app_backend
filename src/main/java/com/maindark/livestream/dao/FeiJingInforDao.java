package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingInfor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FeiJingInforDao {

    @Select("select count(1) from fei_jing_infor where record_id=#{recordId}")
    int queryExisted(@Param("recordId") Integer recordId);


    @Insert("insert into fei_jing_infor(record_id, type,sport_type, title, content) values( #{recordId},#{type},#{sportType} #{title}, #{content})")
    void insertData(FeiJingInfor feiJingInfo);
}
