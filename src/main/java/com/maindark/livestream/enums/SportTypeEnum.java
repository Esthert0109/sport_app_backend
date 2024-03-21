package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum SportTypeEnum {
    FOOTBALL(1,"Football"),BASKETBALL(2,"Basketball");
    private final Integer code;
    private final String message;

    SportTypeEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
