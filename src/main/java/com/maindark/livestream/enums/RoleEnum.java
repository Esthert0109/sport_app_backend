package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    USER("0","normal user"),ANCHOR("1","anchor");

    private final String role;
    private final String msg;
    RoleEnum(String role,String msg) {
        this.role = role;
        this.msg = msg;
    }
}
