package com.maindark.livestream.dao;

import com.maindark.livestream.domain.AllSportsCollection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AllSportsCollectionDao {
    @Insert("insert into all_sports_collection (user_id, match_id, category,status) VALUES (" +
            "#{userId},#{matchId},#{category},#{status})")
    Integer insert(AllSportsCollection allSportsCollection);


    @Delete("delete from all_sports_collection where match_id = #{matchId} and user_id=#{userId}")
    void deleteCollectionByUserIdAndMatchId(Long userId, Long matchId);
}
