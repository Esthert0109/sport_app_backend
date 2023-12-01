package com.maindark.livestream.vo;

import com.maindark.livestream.domain.AllSportsFootballLineUp;
import lombok.Data;

import java.util.List;

@Data
public class AllSportsFootballMatchLineUpVo {
    private List<AllSportsFootballLineUp> homeMatchLineUpList;
    private List<AllSportsFootballLineUp> awayMatchLineList;
}
