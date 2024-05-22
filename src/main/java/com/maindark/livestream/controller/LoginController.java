package com.maindark.livestream.controller;

import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LoginService;
import com.maindark.livestream.vo.LoginVo;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin
public class LoginController {

    @Resource
    LoginService loginService;

    @PostMapping (value = "/do-login")
    public Result<Map<String,String>> doLogin(HttpServletResponse response, @RequestBody @Valid LoginVo loginVo){
        Map<String,String> map = loginService.login(response,loginVo);
        return Result.success(map);
    }
}
