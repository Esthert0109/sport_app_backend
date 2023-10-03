package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class LiveStreamCollection {
    @Id
    private Integer id;
    private Long userId;
    private Integer matchId;
    private String category;
    private String status;
    /*0 football 1 basketball*/


}
