package com.maindark.livestream.vo;

import jakarta.validation.constraints.NotNull;

public class ResetPasswordVo {



    @NotNull(message = "password can be not empty!")
    private String password;




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
