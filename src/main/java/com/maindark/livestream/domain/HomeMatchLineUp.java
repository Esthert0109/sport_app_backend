package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class HomeMatchLineUp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer playerId;
    private Integer matchId;
    private Integer teamId;
    /* if first 0 no 1 yes*/
    private Integer first;
    /* if captain 0 no 1 yes */
    private Integer captain;
    private String playerName;
    private String playerLogo;
    private Integer shirtNumber;
    /* 球员位置，F前锋、M中场、D后卫、G守门员、其他为未知 */
    private String position;
    private String rating;
    private Integer x;
    private Integer y;

}
