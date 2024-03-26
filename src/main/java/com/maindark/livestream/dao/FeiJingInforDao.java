package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingInfor;
import com.maindark.livestream.domain.feijing.InfoCategory;
import com.maindark.livestream.vo.FeiJingInfoVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface FeiJingInforDao {

    @Select("select count(1) from fei_jing_infor where record_id=#{recordId}")
    int queryExisted(@Param("recordId") Integer recordId);


    @Insert("insert into fei_jing_infor(record_id, type,sport_type, title, content) values( #{recordId},#{type},#{sportType},#{title},#{content})")
    void insertData(FeiJingInfor feiJingInfo);

    @Select("select category_id,category from info_category")
    List<InfoCategory> getCategories();

    List<FeiJingInfoVo> selectFeiJingInforList(Map<String, Object> searchMap);

    @Select("select * from fei_jing_infor where id=#{id}")
    FeiJingInfor getInfoById(@Param("id") Integer id);

    List<FeiJingInfoVo> selectFeiJingInforPopularList(Map<String, Object> searchMap);

    List<FeiJingInfoVo> selectFeiJingInforTopList(Map<String, Object> searchMap);
}
