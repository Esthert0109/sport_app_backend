package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class FootballTeam {
    @Id
    private Integer id;
    private Integer competitionId;
    private String nameZh;
    private String nameEn;
    private String logo;
    private Long updatedAt;
}
