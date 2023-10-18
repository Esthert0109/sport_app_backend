package com.maindark.livestream.vo;

import com.maindark.livestream.domain.AllSportsAwayMatchLineUp;
import com.maindark.livestream.domain.AllSportsHomeMatchLineUp;
import lombok.Data;

import java.util.List;

@Data
public class AllSportsFootballMatchLineUpVo {
    private List<AllSportsHomeMatchLineUp> homeMatchLineUpList;
    private List<AllSportsAwayMatchLineUp> awayMatchLineList;
}
