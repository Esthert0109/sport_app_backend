package com.maindark.livestream.service;

import com.maindark.livestream.nami.NamiConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BasketBallService {
    public static String namiUrl = "/api/v5/basketball";
  //   //https://open.sportnanoapi.com/api/v5/basketball/category/list
    @Resource
    NamiConfig namiConfig;






}
