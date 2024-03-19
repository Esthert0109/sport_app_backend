package com.maindark.livestream.vo;

import lombok.Data;


@Data
public class BasketballMatchVo {
    private Integer id;
    private Long competitionId;
    private String competitionName;
    private Long homeTeamId;
    private Long awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Long matchTime;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String status;
    private Integer statusId;
    private String matchTimeStr;
    private String statusStr;
    private String matchDate;
    private Boolean hasCollected;
}
