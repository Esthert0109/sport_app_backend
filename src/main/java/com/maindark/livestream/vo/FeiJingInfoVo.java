package com.maindark.livestream.vo;

import lombok.Data;


@Data
public class FeiJingInfoVo {
    private Integer id;
    private Integer recordId;
    private Integer categoryId;
    private Integer type;
    private Integer sportType;
    private String title;
    private String content;
    private String imgUrl;
    private String popular;
    private Integer readCount;
}
