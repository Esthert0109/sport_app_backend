package com.maindark.livestream.service;

import com.maindark.livestream.dao.FeiJingBasketballAnimationDao;
import com.maindark.livestream.dao.FeiJingFootballAnimationDao;
import com.maindark.livestream.feiJing.FeiJingConfig;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.springframework.stereotype.Service;

@Service
public class FeijingAnimationService {

    @Resource
    FeiJingConfig feiJingConfig;

    @Resource
    FeiJingFootballAnimationDao feiJingFootballAnimationDao;

    @Resource
    FeiJingBasketballAnimationDao feiJingBasketballAnimationDao;


    public String getAnimationUrl(String sportType, String matchId) {
        String url = null;
        long time = System.currentTimeMillis();
        if(StringUtils.equals("1",sportType)) {
            int existed = feiJingFootballAnimationDao.queryExisted(Integer.parseInt(matchId));
            if(existed > 0) {
                String auth = feiJingConfig.getAnimationAccessKey()+ time +feiJingConfig.getAnimationSecretKey();
                auth = DigestUtils.md5Hex(auth).toUpperCase();
                url = "https://zhibo.feijing88.com/football/detail.html?matchId="+matchId+"&accessKey="+ feiJingConfig.getAnimationAccessKey()+"&ts="+ time +"&auth=" +auth;
            }
        }
        if(StringUtils.equals("2",sportType)) {
            int existed = feiJingBasketballAnimationDao.queryExisted(Integer.parseInt(matchId));
            if(existed > 0) {
                String auth = feiJingConfig.getAnimationAccessKey()+ time +feiJingConfig.getAnimationSecretKey();
                auth = DigestUtils.md5Hex(auth).toUpperCase();
                url = "https://zhibo.feijing88.com/basketball/detail.html?matchId="+matchId+"&accessKey="+ feiJingConfig.getAnimationAccessKey()+"&ts="+ time +"&auth=" +auth;
            }
        }
        return url;
    }
}
