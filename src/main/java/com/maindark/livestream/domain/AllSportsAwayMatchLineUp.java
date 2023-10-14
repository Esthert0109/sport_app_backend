package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsAwayMatchLineUp {
    @Id
    private Long id;
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
