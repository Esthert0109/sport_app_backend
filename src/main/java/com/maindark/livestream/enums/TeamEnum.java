package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum TeamEnum {
    HOME("0","homeTeam"),
    AWAY("1","awayTeam");
    private String code;
    private String message;
    TeamEnum(String code,String message){
        this.code = code;
        this.message = message;
    }
}
