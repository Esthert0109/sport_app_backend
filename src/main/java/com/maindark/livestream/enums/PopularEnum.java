package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum PopularEnum {
    NO("0","is not popular"),
    YES("1","popular");
    private final String code;
    private final String message;
    PopularEnum(String code,String message){
        this.code = code;
        this.message = message;
    }
}
