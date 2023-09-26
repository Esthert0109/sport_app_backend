package com.maindark.livestream.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.nami.NamiConfig;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BasketBallService {


    @Resource
    NamiConfig namiConfig;

    public String getBasketBallMatchList(String competitionDate){
        String url = namiConfig.getHost() + namiConfig.getBasketballUrl() + "?user=" + namiConfig.getUser() +"&secret=" + namiConfig.getSecretKey()+"&date="+competitionDate;
        String result = HttpUtil.getNaMiData(url);
        JSONObject resultObj = JSON.parseObject(result);
        Integer code = (Integer)resultObj.get("code");
        if(0 == code) {
            return result;
        } else {
            log.error("nami basketball error result :{}",result);
            throw new GlobalException(CodeMsg.BASKET_BALL_ERROR);
        }
    }




}
