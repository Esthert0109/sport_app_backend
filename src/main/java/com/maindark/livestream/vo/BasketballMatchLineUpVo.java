package com.maindark.livestream.vo;

import com.maindark.livestream.domain.BasketballLineUp;
import lombok.Data;

import java.util.List;

@Data
public class BasketballMatchLineUpVo {
    private List<BasketballLineUp> home;
    private List<BasketballLineUp> away;
}
