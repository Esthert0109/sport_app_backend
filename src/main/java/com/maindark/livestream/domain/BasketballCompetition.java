package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BasketballCompetition {
    @Id
    private Long competitionId;
    private Integer categoryId;
    private String nameZh;
    private String nameEn;
    private String logo;
    private Integer type;
    private Long updatedAt;
}
