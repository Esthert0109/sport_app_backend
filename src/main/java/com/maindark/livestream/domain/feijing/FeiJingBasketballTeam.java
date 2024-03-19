package com.maindark.livestream.domain.feijing;

import lombok.Data;

@Data
public class FeiJingBasketballTeam {
    private Integer teamId;
    private Integer leagueId;
    private String nameCn;
    private String nameEn;
    private String logo;
    private String coachCn;
    private String coachEn;
    private String coachId;
}
