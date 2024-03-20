package com.maindark.livestream.domain.feijing;

import lombok.Data;

import java.util.Date;

@Data
public class FeiJingLiveAddress {
    private Integer id;
    // 1-足球、2-篮球、3-网球
    private Integer sportId;
    private Integer matchId;
    private String pushUrl1;
    private String pushUrl2;
    private String pushUrl3;
}
