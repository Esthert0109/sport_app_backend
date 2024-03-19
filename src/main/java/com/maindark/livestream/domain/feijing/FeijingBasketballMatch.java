package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeijingBasketballMatch {

    private Integer matchId;
    private Integer competitionId;
    private String leagueEn;
    private String leagueChs;
    private String matchTime;
    private Integer matchState;
    private Integer homeTeamId;
    private String homeTeamEn;
    private String homeTeamChs;
    private Integer awayTeamId;
    private String awayTeamEn;
    private String awayTeamChs;
    private String homeScore;
    private String awayScore;
    private String season;
    private String kind;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String updatedDate;

}


