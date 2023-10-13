package com.maindark.livestream.config;

import com.alibaba.druid.util.StringUtils;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.service.LiveStreamUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    LiveStreamUserService liveStreamUserService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == LiveStreamUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

       HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
       HttpServletResponse response =webRequest.getNativeResponse(HttpServletResponse.class);
       String paramToken = request.getParameter(LiveStreamUserService.COOK_NAME_TOKEN);
       String cookieToken = getCookieValue(request,LiveStreamUserService.COOK_NAME_TOKEN);
       String headToken = request.getHeader(LiveStreamUserService.COOK_NAME_TOKEN);
       /*if(StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(headToken)){
           throw new GlobalException(CodeMsg.LOGIN_IN);
       }*/
       String token = StringUtils.isEmpty(paramToken)? cookieToken:paramToken;
       if(token == null) {
           token = headToken;
       }
       return liveStreamUserService.getByToken(response,token);
    }

    private String getCookieValue(HttpServletRequest request, String cookNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie:cookies) {
                if(cookie.getName().equals(cookNameToken)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
