package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsBasketballMatch {
    @Id
    private Long matchId;
    private Long competitionId;
    private String competitionName;
    private Long homeTeamId;
    private Long awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String matchDate;
    private String matchTime;
    private String status;
    private String season;
    private Integer homeScore;
    private Integer awayScore;
    private String eventLive;



}
