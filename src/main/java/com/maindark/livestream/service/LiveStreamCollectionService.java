package com.maindark.livestream.service;

import com.maindark.livestream.dao.BasketballMatchDao;
import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.LiveStreamCollectionDao;
import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.util.BasketballMatchStatus;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LiveStreamCollectionService {


    @Resource
    LiveStreamCollectionDao liveStreamCollectionDao;


    @Resource
    RedisService redisService;

    @Resource
    FootballMatchDao footballMatchDao;

    @Resource
    BasketballMatchDao basketballMatchDao;

    public List<FootballMatchVo> getAllFootballCollectionByUserId(Long userId) {
        return footballMatchDao.getAllFootballCollections(userId);
    }

    public List<FootballMatchVo> getThreeFootballCollectionsByUserId(Long userId) {
        return footballMatchDao.getThreeFootballCollections(userId);
    }

    public List<BasketballMatchVo> getAllBasketballCollectionByUserId(Long userId) {
        return basketballMatchDao.getAllBasketballCollectionsByUserId(userId);
    }

    public List<BasketballMatchVo> getThreeBasketballCollectionsByUserId(Long userId) {
        return basketballMatchDao.getThreeBasketballCollectionsByUserId(userId);
    }

    public FootballMatchVo getFootballMatchByMatchId(Integer matchId) {
        FootballMatchVo match = redisService.get(FootballMatchKey.matchVoKey, String.valueOf(matchId), FootballMatchVo.class);
        if (match == null) {
            match = footballMatchDao.getFootballMatchVoById(matchId);
            redisService.set(FootballMatchKey.matchVoKey, String.valueOf(matchId), match);
        }
        return match;
    }

    public BasketballMatchVo getBasketballMatchByMatchId(Integer matchId) {
        BasketballMatchVo basketballMatchVo = basketballMatchDao.getBasketballVoByMatchId(Long.parseLong("" + matchId));
        if (basketballMatchVo != null) {
            basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatusId()));
            basketballMatchVo.setMatchTimeStr(DateUtil.interceptTime(basketballMatchVo.getMatchTime() * 1000));
            basketballMatchVo.setMatchDate(DateUtil.convertLongTimeToMatchDate(basketballMatchVo.getMatchTime() * 1000));
        }
        return basketballMatchVo;
    }

    public void deleteCollectionById(Long userId, Integer id) {
        liveStreamCollectionDao.deleteCollectionById(userId, id);
    }


    public LiveStreamCollection createCollection(Long userId, CollectionForm collectionForm) {
        LiveStreamCollection liveStreamCollection = new LiveStreamCollection();
        liveStreamCollection.setUserId(userId);
        liveStreamCollection.setMatchId(Integer.parseInt(collectionForm.getMatchId()));
        liveStreamCollection.setCategory(collectionForm.getCategory());
        liveStreamCollection.setStatus(StatusEnum.UP.getCode());
        liveStreamCollectionDao.insert(liveStreamCollection);
        return liveStreamCollection;
    }


}
