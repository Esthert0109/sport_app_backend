package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class LiveStreamDetail {
    @Id
    private Integer id;
    private String cover;
    private String title;
    private String pushHost;
    private String pushCode;
    private Long userId;
    /*0 football 1 basketball*/
    private String sportType;
    /*0 不热门 1 热门*/
    private String isPopular;
    private Date liveDate;
}
