package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LiveStreamDetailForm {
    @NotNull(message = "pushHost can be not empty!")
    private String pushHost;
    @NotNull(message = "pushCode can be not empty!")
    private String pushCode;
    @NotNull(message = "cover can be not empty!")
    private String cover;
    @NotNull(message = "createTime can be not empty!")
    private String createTime;
    @NotNull(message = "title can be not empty!")
    private String title;

}
