package com.maindark.livestream.service;

import com.maindark.livestream.dao.AllSportsBasketballMatchDao;
import com.maindark.livestream.dao.AllSportsCollectionDao;
import com.maindark.livestream.dao.AllSportsFootballMatchDao;
import com.maindark.livestream.domain.AllSportsCollection;
import com.maindark.livestream.enums.EntityTypeEnum;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.util.StreamToListUtil;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class AllSportsCollectionService {

    @Resource
    AllSportsCollectionDao allSportsCollectionDao;

    @Resource
    AllSportsFootballMatchDao allSportsFootballMatchDao;

    @Resource
    AllSportsBasketballMatchDao allSportsBasketballMatchDao;

    @Resource
    FollowService followService;

    public Map<String,List<FootballMatchVo>> getAllFootballCollectionByUserId(Long userId, Pageable pageable) {
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<FootballMatchVo> list = allSportsFootballMatchDao.getAllSportsMatchByUserId(userId,limit,offset);
        Set<String> set = new LinkedHashSet<>();
        list.forEach(footballMatchVo -> set.add(footballMatchVo.getMatchDate()));
        Map<String,List<FootballMatchVo>> map = new HashMap<>();
        set.forEach(date ->{
            Stream<FootballMatchVo> s = list.stream().filter(footballMatchVo -> footballMatchVo.getMatchDate().equals(date));
            List<FootballMatchVo> dates = StreamToListUtil.getArrayListFromStream(s);
            map.put(date,dates);
        });
        return map;
    }

    public List<FootballMatchVo> getThreeFootballCollectionsByUserId(Long userId) {
        return allSportsFootballMatchDao.getThreeCollectionsByUserId(userId);
    }

    public Map<String,List<BasketballMatchVo>> getAllBasketballCollectionByUserId(Long userId,Pageable pageable) {
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        List<BasketballMatchVo> list = allSportsBasketballMatchDao.getAllSportsMatchByUserId(userId,limit,offset);
        Set<String> set = new LinkedHashSet<>();
        list.forEach(footballMatchVo -> set.add(footballMatchVo.getMatchDate()));
        Map<String,List<BasketballMatchVo>> map = new HashMap<>();
        set.forEach(date ->{
            Stream<BasketballMatchVo> s = list.stream().filter(footballMatchVo -> footballMatchVo.getMatchDate().equals(date));
            List<BasketballMatchVo> dates = StreamToListUtil.getArrayListFromStream(s);
            map.put(date,dates);
        });
        return map;
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
        // cancel follow
        followService.unfollow(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(),matchId.intValue());
    }

    public AllSportsCollection createCollection(Long userId, CollectionForm collectionForm) {
        AllSportsCollection allSportsCollection = new AllSportsCollection();
        allSportsCollection.setCategory(collectionForm.getCategory());
        allSportsCollection.setUserId(userId);
        allSportsCollection.setMatchId(Long.parseLong(collectionForm.getMatchId()));
        allSportsCollection.setStatus(StatusEnum.UP.getCode());
        allSportsCollectionDao.insert(allSportsCollection);
        followService.follow(userId.intValue(), EntityTypeEnum.MATCH_EN.getCode(),Integer.parseInt(collectionForm.getMatchId()));
        log.info("collection-en-match:{}",collectionForm.getMatchId());
        return allSportsCollection;
    }




}
