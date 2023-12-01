package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsFootballLineUp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    /*主队，客队 0 主队，1 客队*/
    private Integer type;
    private Long playerId;
    private Long matchId;
    private Long teamId;
    private int first;
    private int captain;
    private String playerName;
    private String playerLogo;
    private int shirtNumber;
    private int position;
    private String rating;
}
