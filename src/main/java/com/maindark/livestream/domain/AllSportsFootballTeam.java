package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsFootballTeam {
    @Id
    private Integer id;
    private Integer competitionId;
    private String teamName;
    private String teamLogo;
    private String coachName;

}
