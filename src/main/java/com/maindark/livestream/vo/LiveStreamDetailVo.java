package com.maindark.livestream.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LiveStreamDetailVo {
    private Integer id;
    private Long userId;
    private String cover;
    private String title;
    private String pushHost;
    private String pushCode;
    /*0 不热门 1 热门*/
    private String isPopular;
    private String liveDate;
}
