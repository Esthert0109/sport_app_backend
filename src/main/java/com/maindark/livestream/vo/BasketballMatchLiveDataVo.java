package com.maindark.livestream.vo;

import lombok.Data;

@Data
public class BasketballMatchLiveDataVo {
    private Long matchId;
    private Long matchTime;
    private Integer status;
    private String homeTeamName;
    private String awayTeamName;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private String matchTimeStr;
    private String matchDate;
    private String statusStr;
    private Integer hFQuarter;
    private Integer hSQuarter;
    private Integer hTQuarter;
    private Integer h4Quarter;
    private Integer aFQuarter;
    private Integer aSQuarter;
    private Integer aTQuarter;
    private Integer a4Quarter;
    /* 罚球进球数 */
    private Integer hFreeThrow;
    /*剩余暂停数*/
    private Integer hNumPauseRemain;
    /*  犯规数 */
    private Integer hNumOfFouls;
    /* 罚球命中率 */
    private Integer hFreeThrowPercentage;
    /* 总暂停数 */
    private Integer hTotalPause;
    /* 3分球进球数*/
    private Integer hThreeGoal;
    /* 2分球进球数*/
    private Integer hTwoGoal;
    private Integer aFreeThrow;
    private Integer aNumPauseRemain;
    private Integer aNumOfFouls;
    private Integer aFreeThrowPercentage;
    private Integer aTotalPause;
    private Integer aThreeGoal;
    private Integer aTwoGoal;
    private Integer homeScore;
    private Integer awayScore;

}
