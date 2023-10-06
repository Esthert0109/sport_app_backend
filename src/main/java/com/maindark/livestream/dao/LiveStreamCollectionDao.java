package com.maindark.livestream.dao;

import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.vo.FootballMatchVo;
import com.maindark.livestream.vo.LiveStreamCollectionVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LiveStreamCollectionDao {

    @Insert("insert live_stream_collection(user_id,match_id,category,status) values " +
            "(#{userId},#{matchId},#{category},#{status})")
    Integer insert(LiveStreamCollection liveStreamCollection);

    @Select("select t1.id,t1.competition_id,t1.home_team_id,t1.away_team_id," +
            "t1.home_team_name,t1.away_team_name,t1.home_team_score,t1.away_team_score," +
            "t1.match_time,fc.name_zh as competitionName,t1.status_id,t1.line_up " +
            "from live_stream.football_match t1 left join live_stream.football_competition fc" +
            " on t1.competition_id = fc.id left join live_stream_collection co on t1.id= co.match_id " +
            "where co.user_id=#{userId}")
    List<FootballMatchVo> getAllCollections(@Param("userId") Long userId);
    @Select("select * from live_stream_collection where match_id=#{id}")
    LiveStreamCollectionVo getCollectionByMatchId(@Param("id") Integer id);

    @Delete("update live_stream_collection set status='0' where id=#{id}")
    int deleteCollectionById(@Param("id") Integer id);


    @Select("select t1.id,t1.competition_id,t1.home_team_id,t1.away_team_id," +
            "t1.home_team_name,t1.away_team_name,t1.home_team_score,t1.away_team_score,t1.match_time," +
            "fc.name_zh as competitionName,t1.status_id,t1.line_up from live_stream.football_match t1 " +
            "left join live_stream.football_competition fc on t1.competition_id = fc.id left join " +
            "live_stream_collection co on t1.id= co.match_id where co.user_id=#{userId} limit 3")
    List<FootballMatchVo> getThreeCollections(@Param("userId") Long userId);


}
