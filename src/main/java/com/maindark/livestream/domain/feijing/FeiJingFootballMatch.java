package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingFootballMatch {
    private Integer matchId;
    private String season;
    private Integer competitionId;
    private String leagueEn;
    private String leagueEnShort;
    private String leagueCnsShort;
    /*1 联赛 2杯赛*/
    private Integer kind;
    /* match status */
    private Integer statusId;
    private String matchDate;
    private String matchTime;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamNameEn;
    private String homeTeamNameCn;
    private String awayTeamNameEn;
    private String awayTeamNameCn;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private String homeFormation;
    private String awayFormation;
    private String venue;
    /*if there is a line-up 0 no 1 yes*/
    private Integer lineUp;
    private String updatedTime;
}
