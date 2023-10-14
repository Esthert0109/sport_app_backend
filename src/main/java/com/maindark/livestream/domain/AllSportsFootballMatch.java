package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsFootballMatch {
    @Id
    private Long id;
    private Long competitionId;
    private Long homeTeamId;
    private Long awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private String competitionName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String matchTime;
    private String status;
    private Integer lineUp;
    private String refereeName;
    private String venueName;
    private String homeFormation;
    private String awayFormation;
    private String matchDate;
    private String eventLive;


}
