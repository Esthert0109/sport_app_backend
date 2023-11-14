package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum IsFirst {
    NO(0,"is not first"),
    YES(1,"is first");
    private final Integer code;
    private final String message;
    IsFirst(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
