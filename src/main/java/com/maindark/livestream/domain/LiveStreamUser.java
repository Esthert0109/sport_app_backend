package com.maindark.livestream.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


import java.util.Date;

@Entity
@Data
public class LiveStreamUser {

    @Id
    private Long id;
    private String nickName;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginTotal;
    private String areaCode;
    private String role;


}
