package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsBasketballLineUp {
    @Id
    private Integer id;
    /*主队，客队 0 主队，1 客队*/
    private Integer type;
    private Long playerId;
    /*比赛id*/
    private Long matchId;

    /*球员姓名*/
    private String playerName;
    /*出场时间*/
    private String minutes;
    /*得分*/
    private String point;
    /*助攻*/
    private String assists;
    /*抢断*/
    private String steals;
    /*篮板 */
    private String totalRebounds;
    /*罚球 总数*/
    private String freeThrowsGoalsAttempts;
    /*罚球命中*/
    private String freeThrowsGoalsMade;
    /*犯规*/
    private String personalFouls;
    /*失误*/
    private String turnovers;
    /*三分 总数*/
    private String threePointGoalsAttempts;
    /*三分命中*/
    private String threePointGoalsMade;
    /*盖帽*/
    private String blocks;
    /*投篮总数*/
    private String fieldGoalsAttempts;
    /*投篮命中*/
    private String fieldGoalsMade;
}
