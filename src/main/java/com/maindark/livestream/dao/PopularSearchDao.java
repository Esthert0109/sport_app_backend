package com.maindark.livestream.dao;

import com.maindark.livestream.domain.PopularSearch;
import com.maindark.livestream.vo.PopularSearchVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PopularSearchDao {
    @Select("select popular_keywords from popular_search ")
    List<PopularSearchVo> getList();

    @Insert("insert into popular_search(popular_keywords, create_date) values (#{popularKeywords},#{createDate})")
    void insertData(PopularSearch popularSearch);
}
