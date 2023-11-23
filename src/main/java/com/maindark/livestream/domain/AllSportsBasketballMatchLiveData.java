package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsBasketballMatchLiveData {
    @Id
    private Integer id;
    private Long matchId;
    private String status;
    private String matchTime;
    private String matchDate;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private Integer hFQuarter;
    private Integer hSQuarter;
    private Integer hTQuarter;
    private Integer h4Quarter;
    private Integer aFQuarter;
    private Integer aSQuarter;
    private Integer aTQuarter;
    private Integer a4Quarter;
    private String hBlocks;
    private String hFieldGoals;
    private String hFreeThrows;
    private String hPersonalFouls;
    private String hRebounds;
    private String hSteals;
    private String hThreePointGoals;
    private String hTurnOvers;
    private String aBlocks;
    private String aFieldGoals;
    private String aFreeThrows;
    private String aPersonalFouls;
    private String aRebounds;
    private String aSteals;
    private String aThreePointGoals;
    private String aTurnOvers;
    private Integer homeScore;
    private Integer awayScore;


}
