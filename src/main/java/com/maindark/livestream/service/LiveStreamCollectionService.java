package com.maindark.livestream.service;

import com.maindark.livestream.dao.FootballMatchDao;
import com.maindark.livestream.dao.LiveStreamCollectionDao;
import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.enums.StatusEnum;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.redis.FootballMatchKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.vo.LiveStreamCollectionVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LiveStreamCollectionService {


   @Resource
    LiveStreamCollectionDao liveStreamCollectionDao;

   @Resource
    NamiConfig namiConfig;

   @Resource
    RedisService redisService;

   @Resource
    FootballMatchDao footballMatchDao;

   public List<LiveStreamCollectionVo> getAllCollectionByUserId(Long userId){
       return liveStreamCollectionDao.getAllCollections(userId);
   }

   public FootballMatch getFootballMatchByMatchId(Integer matchId){
      FootballMatch match = redisService.get(FootballMatchKey.matchKey,String.valueOf(matchId),FootballMatch.class);
      if(match == null) {
          match = footballMatchDao.getFootballMatchById(matchId);
      }
      return match;
   }

   public void deleteCollectionById(Integer id){
       liveStreamCollectionDao.deleteCollectionById(id);
   }


    public LiveStreamCollection createCollection(Long userId, CollectionForm collectionForm) {
        LiveStreamCollection liveStreamCollection = new LiveStreamCollection();
        liveStreamCollection.setUserId(userId);
        liveStreamCollection.setMatchId(collectionForm.getMatchId());
        liveStreamCollection.setCategory(collectionForm.getCategory());
        liveStreamCollection.setStatus(StatusEnum.UP.getCode());
        liveStreamCollectionDao.insert(liveStreamCollection);
        return liveStreamCollection;
    }
}
