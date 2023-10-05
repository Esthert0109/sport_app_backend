package com.maindark.livestream.vo;

import com.maindark.livestream.domain.AwayMatchLineUp;
import com.maindark.livestream.domain.HomeMatchLineUp;
import lombok.Data;

import java.util.List;

@Data
public class FootballMatchLineUpVo {
    private List<HomeMatchLineUp> homeMatchLineUpList;
    private List<AwayMatchLineUp> awayMatchLineList;
}
