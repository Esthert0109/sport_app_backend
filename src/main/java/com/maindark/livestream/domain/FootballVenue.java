package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FootballVenue {
    @Id
    private Integer id;
    private String nameZh;
    private String nameEn;
    private Integer capacity;
    private Long updatedAt;
}
