package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FootballMatchLiveData {
    @Id
    private Integer id;
    private Integer matchId;
    private Integer statusId;
    private Integer homeTeamId;
    private Integer awayTeamId;
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
