package com.maindark.livestream.service;

import com.maindark.livestream.dao.GameAdDao;
import com.maindark.livestream.vo.GameAdVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class GameAdService {

    @Resource
    GameAdDao gameAdDao;

    public GameAdVo getGameVo(){
        return gameAdDao.getGameAd();
    }
}
