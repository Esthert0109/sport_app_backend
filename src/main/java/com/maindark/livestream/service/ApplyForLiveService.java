package com.maindark.livestream.service;

import com.maindark.livestream.dao.ApplyForLiveDao;
import com.maindark.livestream.domain.ApplyForLive;
import com.maindark.livestream.enums.StatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApplyForLiveService {

    @Resource
    ApplyForLiveDao applyForLiveDao;

    public void createApplyForLive(Long userId){
        ApplyForLive applyForLive = new ApplyForLive();
        applyForLive.setUserId(userId);
        applyForLive.setApplyDate(new Date());
        applyForLive.setStatus(StatusEnum.DOWN.getCode());
        applyForLiveDao.insert(applyForLive);
    }
}
