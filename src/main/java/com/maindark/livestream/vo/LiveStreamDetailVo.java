package com.maindark.livestream.vo;

import lombok.Data;


@Data
public class LiveStreamDetailVo {
    private Integer id;
    private Long userId;
    private String sportType;
    private String cover;
    private String title;
    private String pushHost;
    private String pushCode;
    /*0 不热门 1 热门*/
    private String isPopular;
    private String liveDate;
    private String nickName;
    private String avatar;
}
