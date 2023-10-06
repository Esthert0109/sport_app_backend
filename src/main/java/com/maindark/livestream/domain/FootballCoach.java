package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FootballCoach {
    @Id
    private Integer id;
    private String nameZh;
    private String nameEn;
    private String logo;
    private String preferredFormation;
    private Integer teamId;
    private Long updatedAt;
}
