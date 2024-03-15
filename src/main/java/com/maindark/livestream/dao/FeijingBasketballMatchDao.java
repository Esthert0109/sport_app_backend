package com.maindark.livestream.dao;

import com.maindark.livestream.domain.feijing.FeiJingFootballMatch;
import com.maindark.livestream.domain.feijing.FeijingBasketballMatch;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeijingBasketballMatchDao {

    @Select("select count(1) from fei_jing_basketball_match where match_id=#{matchId}")
    int queryExisted(@Param("matchId") Integer matchId);

    @Insert("insert into fei_jing_basketball_match(match_id, competition_id, league_en, league_chs, match_time, match_state, " +
            "home_team_id, home_team_en, home_team_chs, away_team_id, away_team_en, away_team_chs, " +
            "home_score, away_score, season, kind, updated_date) " +
            " values(#{matchId},#{competitionId},#{leagueEn},#{leagueChs},#{matchTime}" +
            ",#{matchState},#{homeTeamId},#{homeTeamEn},#{homeTeamChs},#{awayTeamId},#{awayTeamEn}," +
            "#{awayTeamChs},#{homeScore},#{awayScore},#{season},#{kind},#{updatedDate})")
    void insertData(FeijingBasketballMatch feijingBasketballMatch);


    @Select("select * from fei_jing_basketball_match where match_state = 0")
    List<FeijingBasketballMatch> getMatchesByState();




}
