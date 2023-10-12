package com.maindark.livestream.service;

import com.alibaba.druid.util.StringUtils;
import com.maindark.livestream.dao.LiveStreamUserDao;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.redis.LoginKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.MD5Util;
import com.maindark.livestream.util.UUIDUtil;
import com.maindark.livestream.vo.LoginVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    public static final String COOK_NAME_TOKEN = "token";

    @Resource
    LiveStreamUserDao liveStreamUserDao;
    @Resource
    RedisService redisService;


    public Map<String,String> login(HttpServletResponse response, LoginVo loginVo) {
        Map<String,String> map = new HashMap<>();
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        LiveStreamUser liveStreamUser = liveStreamUserDao.getById(Long.parseLong(mobile));
        //validate the mobile is exit;
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //validate the password is correct;
        String formPass = loginVo.getPassword();
        String dbPass = liveStreamUser.getPassword();
        String saltDB = liveStreamUser.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass,saltDB);
        if(!StringUtils.equals(dbPass,calcPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //generate token
        String token = UUIDUtil.uuid();
        redisService.set(LoginKey.token,token,liveStreamUser);
        Cookie cookie = new Cookie(COOK_NAME_TOKEN,token);
        cookie.setMaxAge(LoginKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
        map.put("token",token);
        map.put("username",liveStreamUser.getNickName());
        map.put("head",liveStreamUser.getHead());
        return map;
    }

}
