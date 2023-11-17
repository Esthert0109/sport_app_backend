package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AllSportsBasketballMatchLiveData {
    @Id
    private Integer id;
    private Long matchId;


}
