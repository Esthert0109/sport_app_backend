package com.maindark.livestream.service;

import com.maindark.livestream.dao.FeiJingBasketballMatchDao;
import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.LiveStreamCollectionDao;
import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.util.*;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class LiveStreamCollectionService {


    @Resource
    LiveStreamCollectionDao liveStreamCollectionDao;


    @Resource
    RedisService redisService;

    @Resource
    FootballMatchDao footballMatchDao;

    @Resource
    FeiJingBasketballMatchDao feiJingBasketballMatchDao;

    @Resource
    FollowService followService;

    public List<Map<String,Object>> getAllFootballCollectionByUserId(Long userId, Pageable pageable) {
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<Map<String,Object>> res = new ArrayList<>();
        List<FootballMatchVo> list = footballMatchDao.getAllFootballCollections(userId,limit,offset);
        if (list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getFootballMatchVos(list);
            Set<String> set = new LinkedHashSet<>();
            list.forEach(footballMatchVo -> set.add(footballMatchVo.getMatchDate()));
            List<FootballMatchVo> finalList = list;
            set.forEach(date ->{
                Map<String,Object> map = new HashMap<>();
                Stream<FootballMatchVo> s = finalList.stream().filter(footballMatchVo -> footballMatchVo.getMatchDate().equals(date));
                List<FootballMatchVo> dates = StreamToListUtil.getArrayListFromStream(s);
                map.put("date",date);
                map.put("data",dates);
                res.add(map);
            });
        }
        return res;
    }

    public List<FootballMatchVo> getThreeFootballCollectionsByUserId(Long userId) {
        List<FootballMatchVo> list = footballMatchDao.getThreeFootballCollections(userId);
        if (list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getFootballMatchVos(list);
        }
        return list;
    }

    public List<Map<String,Object>> getAllBasketballCollectionByUserId(Long userId,Pageable pageable) {
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<Map<String,Object>> res = new ArrayList<>();
        List<BasketballMatchVo> list = feiJingBasketballMatchDao.getFeiJingMatchByUserId(userId,limit,offset);
        if(list != null && !list.isEmpty()) {
            list = MatchDataConvertUtil.getBasketballMatchVos(list);
            Set<String> set = new LinkedHashSet<>();
            list.forEach(footballMatchVo -> set.add(footballMatchVo.getMatchDate()));
            List<BasketballMatchVo> finalList = list;
            set.forEach(date ->{
                Map<String,Object> map = new HashMap<>();
                Stream<BasketballMatchVo> s = finalList.stream().filter(footballMatchVo -> footballMatchVo.getMatchDate().equals(date));
                List<BasketballMatchVo> dates = StreamToListUtil.getArrayListFromStream(s);
                map.put("date",date);
                map.put("data",dates);
                res.add(map);
            });
        }
        return res ;
    }

    public List<BasketballMatchVo> getThreeBasketballCollectionsByUserId(Long userId) {
        List<BasketballMatchVo> list = feiJingBasketballMatchDao.getThreeCollectionsByUserId(userId);
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
                match.setStatusStr(FootballMatchStatus.convertStatusIdToStr(match.getStatusId()));
                match.setMatchTimeStr(DateUtil.interceptTime(match.getMatchTime() * 1000));
                redisService.set(FootballMatchKey.matchVoKey, String.valueOf(matchId), match);
            }
        }
        return match;
    }

    public BasketballMatchVo getBasketballMatchByMatchId(Integer matchId) {
        BasketballMatchVo basketballMatchVo = feiJingBasketballMatchDao.getBasketballMatchVoByMatchId(matchId);
        if (basketballMatchVo != null) {
            basketballMatchVo.setStatusStr(BasketballMatchStatus.convertStatusIdToStr(basketballMatchVo.getStatus()));
            basketballMatchVo.setMatchTimeStr(DateUtil.interceptTime(basketballMatchVo.getMatchTime() * 1000));
            basketballMatchVo.setMatchDate(DateUtil.convertLongTimeToMatchDate(basketballMatchVo.getMatchTime() * 1000));
        }
        return basketballMatchVo;
    }

    public void deleteCollectionById(Long userId, Integer id) {
        liveStreamCollectionDao.deleteCollectionById(userId, id);
        // cancel follow
        followService.unfollow(userId.intValue(), EntityTypeEnum.MATCH_CN.getCode(),id);
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
