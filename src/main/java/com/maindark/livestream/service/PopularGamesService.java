package com.maindark.livestream.service;

import com.maindark.livestream.dao.PopularGamesDao;
import com.maindark.livestream.vo.PopularGamesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularGamesService {

    private PopularGamesDao popularGamesDao;

    public List<PopularGamesVo> getAllPopularGames() {
        return popularGamesDao.getAllPopularGames();
    }
}
