package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    UP("0","normal"),
    DOWN("1","abnormal");
    private String code;
    private String message;
    StatusEnum(String code,String message){
        this.code = code;
        this.message = message;
    }


}
