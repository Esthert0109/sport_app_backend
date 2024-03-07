package com.maindark.livestream.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PopularGamesVo {
    private Integer id;
    private String gameNameEn;
    private String gameNameCn;
    private String gameLogo;
    private String gameAndroidUrl;
    private String gameIphoneUrl;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
