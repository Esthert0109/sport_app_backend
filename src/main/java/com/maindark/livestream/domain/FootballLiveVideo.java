package com.maindark.livestream.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class FootballLiveVideo {
    @Id
    private Integer id;
    private Integer matchId;
    /* type 类型，1-集锦、2-录像 */
    private Integer type;
    private String title;
    private String mobileLink;
    private String pcLink;
    private String cover;
    private Long duration;
}
