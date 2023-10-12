package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsFootballCompetition {
    @Id
    private Integer id;
    private String competitionName;
    private String logo;
    private String country;
    private String countryLogo;
}
