package com.maindark.livestream.vo;

import com.maindark.livestream.domain.feijing.FeiJingFootballLineUp;
import lombok.Data;

import java.util.List;

@Data
public class FeiJingFootballMatchLineUpVo {
    private List<FeiJingFootballLineUp> homeMatchLineUpList;
    private List<FeiJingFootballLineUp> awayMatchLineList;
}
