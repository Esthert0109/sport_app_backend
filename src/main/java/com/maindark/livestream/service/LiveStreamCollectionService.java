package com.maindark.livestream.service;

import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.LiveStreamCollectionDao;
import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
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

   public List<FootballMatchVo> getAllCollectionByUserId(Long userId){
       return liveStreamCollectionDao.getAllCollections(userId);
   }

    public List<FootballMatchVo> getThreeCollectionsByUserId(Long userId){
        return liveStreamCollectionDao.getThreeCollections(userId);
    }

   public FootballMatchVo getFootballMatchByMatchId(Integer matchId){
      FootballMatchVo match = redisService.get(FootballMatchKey.matchVoKey,String.valueOf(matchId),FootballMatchVo.class);
      if(match == null) {
          match = footballMatchDao.getFootballMatchVoById(matchId);
          redisService.set(FootballMatchKey.matchVoKey,String.valueOf(matchId),match);
      }
      return match;
   }

   public void deleteCollectionById(Long userId,Integer id){
       liveStreamCollectionDao.deleteCollectionById(userId,id);
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
