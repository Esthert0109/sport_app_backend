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
    private Integer userId;
    private Integer competitionId;
    /* 0 football 1 basketball*/
    private String category;
    private Date createDate;

}
