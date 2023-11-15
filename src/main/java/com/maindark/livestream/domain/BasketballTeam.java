package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BasketballTeam {
    @Id
    private Long teamId;
    private Long competitionId;
    /*赛区id，1-大西洋赛区、2-中部赛区、3-东南赛区、4-太平洋赛区、5-西北赛区、6-西南赛区、
    7-A组赛区、8-B组赛区、9-C组赛区、10-D组赛区（1~6:NBA 7~10:CBA）、0-无*/
    private Integer conferenceId;
    private String nameZh;
    private String nameEn;
    private String logo;
    private Long updatedAt;
}
