package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class LiveStreamMessage {

    @Id
    private Integer id;
    private String content;
    private String summary;
    private Date createDate;
    private Date editDate;
    private String status;
}
