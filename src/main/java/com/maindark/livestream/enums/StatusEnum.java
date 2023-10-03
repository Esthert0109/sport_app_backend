package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    DOWN("0","abnormal"),
    UP("1","normal");
    private String code;
    private String message;
    StatusEnum(String code,String message){
        this.code = code;
        this.message = message;
    }


}
