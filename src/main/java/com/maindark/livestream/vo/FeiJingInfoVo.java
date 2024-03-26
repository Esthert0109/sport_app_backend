package com.maindark.livestream.vo;

import lombok.Data;

import java.util.Date;


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
    private Date createdDate;
    private String dateStr;
    // 0 false 1 true
    private String isTop;
}
