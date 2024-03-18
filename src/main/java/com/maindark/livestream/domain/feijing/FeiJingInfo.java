package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingInfo {

    private Integer recordId;
    private Integer matchId;
    private Integer leagueId;
    private String leagueName;
    private String homeTeam;
    private String awayTeam;
    private Integer type;
    private String title;
    private String content;
    private String updateTime;
}
