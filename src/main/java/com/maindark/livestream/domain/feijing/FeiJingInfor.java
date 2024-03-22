package com.maindark.livestream.domain.feijing;

import lombok.Data;

import java.util.Date;

@Data
public class FeiJingInfor {
    private Integer id;
    private Integer recordId;
    private Integer categoryId;
    private Integer type;
    private Integer sportType;
    private String title;
    private String content;
    private String imgUrl;
    private String popular;
    private Date createdDate;
}
