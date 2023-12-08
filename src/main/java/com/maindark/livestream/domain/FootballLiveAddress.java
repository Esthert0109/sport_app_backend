package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class FootballLiveAddress {
    @Id
    private Integer id;
    /* 1 football 2 basketball*/
    private Integer sportId;
    private Integer matchId;
    private Long matchTime;
    private Integer matchStatus;
    private Integer compId;
    private String comp;
    private String homeTeam;
    private String awayTeam;
    private String pushUrl1;
    private String pushUrl2;
    private String pushUrl3;
    private Date createDate;
}
