package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BasketballMatchLiveData {
    @Id
    private Integer id;
    private Long matchId;
    private Integer hFQuarter;
    private Integer hSQuarter;
    private Integer hTQuarter;
    private Integer h4Quarter;
    private Integer aFQuarter;
    private Integer aSQuarter;
    private Integer aTQuarter;
    private Integer a4Quarter;
    /* 罚球进球数 */
    private Integer hPKickGoal;
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
    private Integer aPKickGoal;
    private Integer aNumPauseRemain;
    private Integer aNumOfFouls;
    private Integer aFreeThrowPercentage;
    private Integer aTotalPause;
    private Integer aThreeGoal;
    private Integer aTwoGoal;
    private Integer homeScore;
    private Integer awayScore;


}
