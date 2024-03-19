package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingFootballLineUp {
    private Integer id;
    /*主队，客队 0 主队，1 客队*/
    private Integer type;
    private Integer playerId;
    private Integer matchId;
    private int first;
    private int captain;
    private String playerName;
    private int shirtNumber;
    private int position;
    private String rating;
}
