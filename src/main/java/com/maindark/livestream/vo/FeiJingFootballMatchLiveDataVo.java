package com.maindark.livestream.vo;

import lombok.Data;

@Data
public class FeiJingFootballMatchLiveDataVo {
    private Integer matchId;
    private String status;
    private String matchDate;
    private String matchTime;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String refereeName;
    private String venueName;
    private String homeFormation;
    private String awayFormation;
    private String homeCoach;
    private String awayCoach;
    private Integer homeAttackNum;
    private Integer awayAttackNum;
    private Integer homeAttackDangerNum;
    private Integer awayAttackDangerNum;
    private String homePossessionRate;
    private String awayPossessionRate;
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
    private Integer homePenaltyNum;
    private Integer awayPenaltyNum;
}
