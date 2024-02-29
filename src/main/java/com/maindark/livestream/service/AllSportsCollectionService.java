package com.maindark.livestream.service;

import com.maindark.livestream.dao.AllSportsBasketballMatchDao;
import com.maindark.livestream.dao.AllSportsCollectionDao;
import com.maindark.livestream.dao.AllSportsFootballMatchDao;
import com.maindark.livestream.domain.AllSportsCollection;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllSportsCollectionService {

    @Resource
    AllSportsCollectionDao allSportsCollectionDao;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

    @Resource
    FollowService followService;

    public List<FootballMatchVo> getAllFootballCollectionByUserId(Long userId) {
        return allSportsFootballMatchDao.getAllSportsMatchByUserId(userId);
    }

    public List<FootballMatchVo> getThreeFootballCollectionsByUserId(Long userId) {
        return allSportsFootballMatchDao.getThreeCollectionsByUserId(userId);
    }

    public List<BasketballMatchVo> getAllBasketballCollectionByUserId(Long userId) {
        return allSportsBasketballMatchDao.getAllSportsMatchByUserId(userId);
    }

    public List<BasketballMatchVo> getThreeBasketballCollectionsByUserId(Long userId) {
        return allSportsBasketballMatchDao.getThreeCollectionsByUserId(userId);
    }

    public FootballMatchVo getFootballMatchByMatchId(Long matchId) {
        return allSportsFootballMatchDao.getAllSportsFootballMatchByMatchId(matchId);
    }
    public BasketballMatchVo getBasketballMatchByMatchId(Long matchId) {
        return allSportsBasketballMatchDao.getBasketballMatchVoByMatchId(matchId);
    }

    public void deleteCollectionById(Long userId, Long matchId) {
        allSportsCollectionDao.deleteCollectionByUserIdAndMatchId(userId,matchId);
    }

    public AllSportsCollection createCollection(Long userId, CollectionForm collectionForm) {
        AllSportsCollection allSportsCollection = new AllSportsCollection();
        allSportsCollection.setCategory(collectionForm.getCategory());
        allSportsCollection.setUserId(userId);
        allSportsCollection.setMatchId(Long.parseLong(collectionForm.getMatchId()));
        allSportsCollection.setStatus(StatusEnum.UP.getCode());
        allSportsCollectionDao.insert(allSportsCollection);
        followService.follow(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(),Integer.parseInt(collectionForm.getMatchId()) );
        return allSportsCollection;
    }




}
