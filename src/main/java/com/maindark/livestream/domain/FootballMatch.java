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
    private Integer status;
    private Long matchTime;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    /*if there is a line-up 0 no 1 yes*/
    private Integer lineUp;
    private Long updatedAt;



}
