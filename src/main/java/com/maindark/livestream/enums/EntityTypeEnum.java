package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum EntityTypeEnum {
    MATCH_EN(1,"english match"),MATCH_CN(2,"chinese match"),INFO(3,"info");
    private final Integer code;
    private final String message;

    EntityTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
