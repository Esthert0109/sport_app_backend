package com.maindark.livestream.dao;

import com.maindark.livestream.domain.BasketballMatchLiveData;
import com.maindark.livestream.vo.BasketballMatchLiveDataVo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BasketballMatchLiveDataDao {

    @Insert("insert into basketball_match_live_data(match_id,status, h_f_quarter, h_s_quarter, h_t_quarter, h_4_quarter, " +
            "a_f_quarter, a_s_quarter, a_t_quarter, a_4_quarter, h_P_KickGoal, h_Num_Pause_Remain, h_Num_Of_Fouls, " +
            "h_Free_Throw_Percentage, h_Total_Pause, h_Two_Goal, h_Three_Goal, a_P_KickGoal, a_Num_Pause_Remain, " +
            "a_Num_OfFouls, a_Free_Throw_Percentage, a_Total_Pause, a_Two_Goal, a_Three_Goal, home_score, away_score) VALUES (" +
            "#{matchId},#{status},#{hFQuarter},#{hSQuarter},#{hTQuarter},#{h4Quarter},#{aFQuarter},#{aSQuarter},#{aTQuarter},#{a4Quarter}," +
            "#{hPKickGoal},#{hNumPauseRemain},#{hNumOfFouls},#{hFreeThrowPercentage},#{hTotalPause},#{hTwoGoal}," +
            "#{hThreeGoal},#{aPKickGoal},#{aNumPauseRemain},#{aNumOfFouls},#{aFreeThrowPercentage},#{aTotalPause},#{aTwoGoal}," +
            "#{aThreeGoal},#{homeScore},#{awayScore})")
    void insertData(BasketballMatchLiveData basketballMatchLiveData);

   @Update("update basketball_match_live_data set status=#{status}, h_f_quarter=#{hFQuarter},h_s_quarter=#{hSQuarter},h_t_quarter=#{hTQuarter},h_4_quarter=#{h4Quarter}," +
           "a_f_quarter=#{aFQuarter},a_s_quarter=#{aSQuarter},a_t_quarter=#{aTQuarter},a_4_quarter=#{a4Quarter},h_P_KickGoal=#{hPKickGoal},h_Num_Pause_Remain=#{hNumPauseRemain}," +
           "h_Num_Of_Fouls=#{hNumOfFouls},h_Free_Throw_Percentage=#{hFreeThrowPercentage},h_Total_Pause=#{hTotalPause},a_P_KickGoal=#{aPKickGoal},a_Num_Pause_Remain=#{aNumPauseRemain}," +
           "a_Num_OfFouls=#{hNumOfFouls},a_Free_Throw_Percentage=#{aFreeThrowPercentage},a_Total_Pause=#{aTotalPause},h_Two_Goal=#{hTwoGoal},h_Three_Goal=#{hThreeGoal},a_Two_Goal=#{aTwoGoal}," +
           "a_Three_Goal=#{aThreeGoal},home_score=#{homeScore},away_score=#{awayScore} where match_id=#{matchId}")
   void updateData(BasketballMatchLiveData basketballMatchLiveData);

   @Select("select count(1) from basketball_match_live_data where match_id=#{matchId}")
   int queryExist(@Param("matchId")Long matchId);


   @Select("select match_id,status, h_f_quarter, h_s_quarter, h_t_quarter, h_4_quarter, a_f_quarter, a_s_quarter,a_t_quarter," +
           "a_4_quarter, h_p_kickgoal, h_num_pause_remain, h_num_of_fouls, h_free_throw_percentage, " +
           "h_total_pause, h_two_goal, h_three_goal, a_p_kickgoal, a_num_pause_remain, a_num_offouls, " +
           "a_free_throw_percentage, a_total_pause, a_two_goal, a_three_goal, home_score, away_score " +
           "from basketball_match_live_data where match_id=#{matchId}")
    BasketballMatchLiveDataVo getMatchLiveDataByMatchId(@Param("matchId") Long matchId);
}
