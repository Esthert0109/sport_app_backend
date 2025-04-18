package com.maindark.livestream.vo;

import lombok.Data;

@Data
public class FootballMatchVo {
    private Integer id;
    private Integer competitionId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Long matchTime;
    private String competitionName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private Integer statusId;
    private String matchTimeStr;
    private String statusStr;
    private Integer lineUp;
    private String refereeName;
    private String venueName;
    private String homeFormation;
    private String awayFormation;
    private String matchDate;
    private Boolean hasCollected;
}
