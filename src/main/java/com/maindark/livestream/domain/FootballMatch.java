package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Data;


@Entity
@Data
public class FootballMatch {
    @Id
    private Integer id;
    private Integer seasonId;
    private Integer competitionId;
    /* match status */
    private Integer statusId;
    private Long matchTime;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Integer refereeId;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private String homeFormation;
    private String awayFormation;
    private Integer venueId;
    /*if there is a line-up 0 no 1 yes*/
    private Integer lineUp;
    private Long updatedAt;




}
