package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class AnchorFollower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Long anchorId;
    private Long followerId;
    private Date followDate;
    private Date cancelDate;

}
