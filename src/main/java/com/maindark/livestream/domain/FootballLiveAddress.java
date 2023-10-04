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
    private Integer matchId;
    private Long matchTime;
    private String comp;
    private String homeTeam;
    private String awayTeam;
    private String mobileLink;
    private String pcLink;
    private Date createDate;
}
