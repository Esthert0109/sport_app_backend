package com.maindark.livestream.service;

import com.maindark.livestream.dao.BasketballMatchDao;
import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.LiveStreamCollectionDao;
import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.util.BasketballMatchStatus;
import com.maindark.livestream.util.DateUtil;
import com.maindark.livestream.util.MatchDataConvertUtil;
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

    @Resource
    FollowService followService;

    public List<FootballMatchVo> getAllFootballCollectionByUserId(Long userId) {
        List<FootballMatchVo> list = footballMatchDao.getAllFootballCollections(userId);
        if (list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getFootballMatchVos(list);
        }
        return list;
    }

    public List<FootballMatchVo> getThreeFootballCollectionsByUserId(Long userId) {
        List<FootballMatchVo> list = footballMatchDao.getThreeFootballCollections(userId);
        if (list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getFootballMatchVos(list);
        }
        return list;
    }

    public List<BasketballMatchVo> getAllBasketballCollectionByUserId(Long userId) {
        List<BasketballMatchVo> list = basketballMatchDao.getAllBasketballCollectionsByUserId(userId);
        if(list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getBasketballMatchVos(list);
        }
        return list ;
    }

    public List<BasketballMatchVo> getThreeBasketballCollectionsByUserId(Long userId) {
        List<BasketballMatchVo> list = basketballMatchDao.getThreeBasketballCollectionsByUserId(userId);
        if(list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getBasketballMatchVos(list);
        }
        return list ;
    }

    public FootballMatchVo getFootballMatchByMatchId(Integer matchId) {
        FootballMatchVo match = redisService.get(FootballMatchKey.matchVoKey, String.valueOf(matchId), FootballMatchVo.class);
        if (match == null) {
            match = footballMatchDao.getFootballMatchVoById(matchId);
            if(match != null) {
                match.setMatchDate(DateUtil.convertLongTimeToMatchDate(match.getMatchTime() * 1000));
                match.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(match.getStatusId()));
                match.setMatchTimeStr(DateUtil.interceptTime(match.getMatchTime() * 1000));
                redisService.set(FootballMatchKey.matchVoKey, String.valueOf(matchId), match);
            }
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
        // cancel follow
        followService.unfollow(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(),id);
    }


    public LiveStreamCollection createCollection(Long userId, CollectionForm collectionForm) {
        LiveStreamCollection liveStreamCollection = new LiveStreamCollection();
        liveStreamCollection.setUserId(userId);
        liveStreamCollection.setMatchId(Integer.parseInt(collectionForm.getMatchId()));
        liveStreamCollection.setCategory(collectionForm.getCategory());
        liveStreamCollection.setStatus(StatusEnum.UP.getCode());
        liveStreamCollectionDao.insert(liveStreamCollection);
        followService.follow(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(), Integer.parseInt(collectionForm.getMatchId()));
        return liveStreamCollection;
    }


}
