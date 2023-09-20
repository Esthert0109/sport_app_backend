package com.maindark.livestream.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.maindark.livestream.dao.LiveStreamCollectionDao;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.util.HttpUtil;
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

   public List<LiveStreamCollectionVo> getAllCollectionByUserId(Integer userId){
       return liveStreamCollectionDao.getAllCollection(userId);
   }

   public Object getMatchById(Integer id){
       LiveStreamCollectionVo  liveStreamCollectionVo = liveStreamCollectionDao.getCollectionByCompetitionId(id);
       String category = liveStreamCollectionVo.getCategory();
       // 0 football 1 basketball
       String url;
       if (StringUtils.equals("0",category)) {
            url = namiConfig.getHost()+namiConfig.getFootballUrl()+"&user" + namiConfig.getUser() + "&secret=" + namiConfig.getSecretKey() + "&id" + id;
       } else {
            url = namiConfig.getHost()+namiConfig.getBasketballUrl()+"&user" + namiConfig.getUser() + "&secret=" + namiConfig.getSecretKey() + "&id" + id;
       }
       String result = HttpUtil.getNaMiData(url);
       return JSON.parseObject(result);
   }

   public void deleteCollectionById(Integer id){
       liveStreamCollectionDao.deleteCollectionById(id);
   }


}
