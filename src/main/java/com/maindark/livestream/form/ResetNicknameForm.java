package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetNicknameForm {
    @NotNull(message = "nickname can be not empty!")
    private String nickName;
}
