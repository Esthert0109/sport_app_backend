package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingBasketballMatch {
    private Integer matchId;
    private Integer competitionId;
    private String competitionName;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamCns;
    private String awayTeamCns;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String matchDate;
    private String matchTime;
    private String status;
    private String season;
    private Integer hFQuarter;
    private Integer hSQuarter;
    private Integer hTQuarter;
    private Integer h4Quarter;
    private Integer aFQuarter;
    private Integer aSQuarter;
    private Integer aTQuarter;
    private Integer a4Quarter;
    private Integer homeScore;
    private Integer awayScore;
    private String homeCoach;
    private String awayCoach;
    private Boolean hasState;
}
