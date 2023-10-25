package com.maindark.livestream.domain;

import lombok.Data;

@Data
public abstract class BaseFootballMatchLineup {

    public BaseFootballMatchLineup(){

    }
    private Integer id;
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
