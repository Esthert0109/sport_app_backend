package com.maindark.livestream.vo;

import lombok.Data;

@Data
public class FootballLiveAddressVo {

    private Integer sportId;
    private Integer matchId;
    private String pushUrl1;
    private String pushUrl2;
    private String pushUrl3;
}
