package com.maindark.livestream.vo;

import lombok.Data;

@Data
public class LiveStreamCollectionVo {

    private Integer id;
    private Integer userId;
    private Integer competitionId;
    /* 0 football 1 basketball*/
    private String category;
    private String competitionName;

}
