package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BasketballMatch {
    @Id
    private Long matchId;
    private Long competitionId;
    private Long homeTeamId;
    private Long awayTeamId;
    private Integer kind;
    /*match count period*/
    private Integer periodCount;
    private Integer statusId;
    private Long matchTime;
    private Integer homeScore;
    private Integer awayScore;
    private Integer seasonId;
    private Long updatedAt;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamLogo;
    private String awayTeamLogo;
}
