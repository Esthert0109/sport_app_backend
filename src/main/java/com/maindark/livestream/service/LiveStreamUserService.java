package com.maindark.livestream.service;

import com.alibaba.druid.util.StringUtils;
import com.maindark.livestream.dao.LiveStreamUserDao;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.LiveStreamUserForm;
import com.maindark.livestream.redis.LoginKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.redis.UserKey;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.util.MD5Util;
import com.maindark.livestream.util.UUIDUtil;
import com.maindark.livestream.vo.LiveStreamUserVo;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LiveStreamUserService {
    public static final String COOK_NAME_TOKEN = "token";
    public static final String SALT= "1a2b3c";
    @Resource
    LiveStreamUserDao liveStreamUserDao;
    @Resource
    RedisService redisService;
    public LiveStreamUser getById(long id){
        return liveStreamUserDao.getById(id);
    }

    public LiveStreamUser getByToken(HttpServletResponse response,String token){
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        LiveStreamUser liveStreamUser = redisService.get(LoginKey.token,token,LiveStreamUser.class);
        if(liveStreamUser != null) {
            addCookie(response,liveStreamUser);
        }

        return liveStreamUser;
    }

    public LiveStreamUserVo findById(long id){
        LiveStreamUserVo liveStreamUserVo = new LiveStreamUserVo();
        LiveStreamUser liveStreamUser = liveStreamUserDao.getById(id);
        BeanUtils.copyProperties(liveStreamUser,liveStreamUserVo);
        return liveStreamUserVo;
    }


    public LiveStreamUser save(LiveStreamUserForm liveStreamUserForm) {
        LiveStreamUser liveStreamUser = new LiveStreamUser();
        BeanUtils.copyProperties(liveStreamUserForm,liveStreamUser);
        liveStreamUser.setSalt(SALT);
        liveStreamUser.setPassword(MD5Util.formPassToDBPass(liveStreamUserForm.getPassword(),SALT));
        liveStreamUser.setRegisterDate(new Date());
        liveStreamUserDao.insert(liveStreamUser);
        return liveStreamUser;
    }

    public boolean updatePassword(String token, long id, String formPass) {
        //get user
        LiveStreamUser user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //update database
        LiveStreamUser toBeUpdate = new LiveStreamUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        liveStreamUserDao.update(toBeUpdate);

        //manage cache
        redisService.delete(UserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(LoginKey.token, token, user);
        return true;
    }


    private void addCookie(HttpServletResponse response,LiveStreamUser liveStreamUser){
        String token = UUIDUtil.uuid();
        redisService.set(LoginKey.token,token,liveStreamUser);
        Cookie cookie = new Cookie(COOK_NAME_TOKEN,token);
        cookie.setMaxAge(LoginKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
