package com.maindark.livestream.vo;

import jakarta.validation.constraints.NotNull;

public class ResetPasswordVo {


    @NotNull(message = "msgCode can not be empty!")

    private String msgCode;
    @NotNull
    private String password;


    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
