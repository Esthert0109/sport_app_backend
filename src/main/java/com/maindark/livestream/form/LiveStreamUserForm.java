package com.maindark.livestream.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;
@Data
public class LiveStreamUserForm {
    //id equals mobile
    @NotNull(message = "mobile can be not empty")
    private String id;
    @NotNull(message = "username can be not empty")
    private String nickName;
    @NotNull(message = "password can be not empty")
    private String password;
    private String head;
    private Date registerDate;
    @NotNull(message = "area code can be not empty")
    private String areaCode;



}
