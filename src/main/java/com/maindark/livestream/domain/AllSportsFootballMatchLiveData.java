package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsFootballMatchLiveData {
    @Id
    private Integer id;
    private Long matchId;
    private String status;
    private String matchDate;
    private String matchTime;
    private Long homeTeamId;
    private Long awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeAttackNum;
    private Integer awayAttackNum;
    private Integer homeAttackDangerNum;
    private Integer awayAttackDangerNum;
    private Integer homePossessionRate;
    private Integer awayPossessionRate;
    private Integer homeShootGoalNum;
    private Integer awayShootGoalNum;
    private Integer homeBiasNum;
    private Integer awayBiasNum;
    private Integer homeCornerKickNum;
    private Integer awayCornerKickNum;
    private Integer homeRedCardNum;
    private Integer awayRedCardNum;
    private Integer homeYellowCardNum;
    private Integer awayYellowCardNum;
    private Integer homeScore;
    private Integer awayScore;

}
