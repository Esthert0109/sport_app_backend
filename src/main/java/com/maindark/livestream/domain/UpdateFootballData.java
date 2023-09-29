package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UpdateFootballData {
    @Id
    private Integer id;
    private Integer matchId;
    private Integer seasonId;
    private Integer competitionId;
    private Integer pubTime;
    private Long updateTime;
    /* uniqueKey= matchId+seasonId+competitionId+pubTime+updateTime*/
    private Long uniqueKey;
}
