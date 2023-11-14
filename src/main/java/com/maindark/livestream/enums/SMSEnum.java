package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum SMSEnum {
    REGISTER("1","register send msgCode"),
    RESET("2","reset password send msgCode"),
    FORGOT("3","forgot password send msgCode");

    private final String code;
    private final String msg;

    SMSEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
