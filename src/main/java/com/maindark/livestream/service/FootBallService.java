package com.maindark.livestream.service;

import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FootBallService {
  ////https://open.sportnanoapi.com/api/v5/football/category/list
    @Resource
    NamiConfig namiConfig;

    public String getFootBallMatchList(){
      String url = namiConfig.getHost() + namiConfig.getFootballUrl() + "&user" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey();
      String result = HttpUtil.getNaMiData(url);
      return null;
    }






}
