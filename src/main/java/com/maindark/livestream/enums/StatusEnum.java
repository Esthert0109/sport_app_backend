package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    DOWN("0","abnormal"),
    UP("1","normal");
    private final String  code;
    private final String message;
    StatusEnum(String code,String message){
        this.code = code;
        this.message = message;
    }


}
