package com.maindark.livestream.service;

import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FootBallService {
  ////https://open.sportnanoapi.com/api/v5/football/category/list
    public static String namiUrl = "/api/v5/football";
    @Resource
    NamiConfig namiConfig;

    public String getFootBallMatchList(){
      String url = namiConfig.getHost() + namiUrl + "&user" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey();
      String result = HttpUtil.getNaMiData(url);
      return null;
    }






}
