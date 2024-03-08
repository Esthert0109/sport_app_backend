package com.maindark.livestream.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PopularGamesVo {
    private Integer id;
    private String gameNameEn;
    private String gameNameCn;
    private String gameLogo;
    private String gameAndroidUrl;
    private String gameIphoneUrl;
    private Integer sort;
    private Date createdTime;
    private Date updatedTime;
}
