package com.maindark.livestream.dao;

import com.maindark.livestream.vo.PopularGamesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PopularGamesDao {

    @Select("select * from pop_game")
    List<PopularGamesVo> getAllPopularGames();
}

//    @Select("SELECT * FROM pop_game WHERE id = #{id}")
//    PopularGamesVo getPopularGameById(int id);
