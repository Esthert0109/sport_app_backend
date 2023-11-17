package com.maindark.livestream.enums;

import lombok.Getter;

@Getter
public enum LineUpType {
    HOME(0,"home team"),AWAY(1,"away team");
    private final Integer type;
    private final String msg;
    LineUpType(Integer type,String msg){
        this.type =type;
        this.msg = msg;
    }
}
