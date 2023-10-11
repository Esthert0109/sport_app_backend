package com.maindark.livestream.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SMSValidateVo {
    @NotNull(message = "mobile phone can be not empty!")
    private String mobile;
    @NotNull(message = "msgCode can be not empty!")
    private String code;
}
