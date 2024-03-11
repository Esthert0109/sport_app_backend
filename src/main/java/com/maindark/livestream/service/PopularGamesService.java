package com.maindark.livestream.service;

import com.maindark.livestream.dao.PopularGamesDao;
import com.maindark.livestream.vo.PopularGamesVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularGamesService {

    @Resource
    private PopularGamesDao popularGamesDao;

    public List<PopularGamesVo> getAllPopularGames() {
        return popularGamesDao.getAllPopularGames();
    }
}
