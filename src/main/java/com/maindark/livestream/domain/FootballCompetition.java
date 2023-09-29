package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class FootballCompetition {
    @Id
    private Integer id;
    private String logo;
    private String nameZh;
    private String nameEn;
    private String shortNameZh;
    private String shortNameEn;
    //赛事类型，0-未知、1-联赛、2-杯赛、3-友谊赛
    private Integer type;
    private long updatedAt;
}
