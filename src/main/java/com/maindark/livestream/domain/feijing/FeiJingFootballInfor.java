package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingFootballInfor {

    private Integer recordId;
    private Integer type;
    private String title;
    private String content;
    private String imgUrl;
    private Integer categoryId;
}
