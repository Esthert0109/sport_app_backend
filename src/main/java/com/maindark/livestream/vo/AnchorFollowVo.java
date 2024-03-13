package com.maindark.livestream.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AnchorFollowVo {


    private Long id;
    private Long anchorId;
    private LiveStreamUserVo anchorDetails;
    private Long followerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followCreatedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followUpdatedTime;
}
