package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.LiveStreamUserForm;
import com.maindark.livestream.form.ResetHeadForm;
import com.maindark.livestream.redis.LoginKey;
import com.maindark.livestream.redis.RedisService;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LiveStreamUserService;
import com.maindark.livestream.vo.LiveStreamUserVo;
import com.maindark.livestream.vo.ResetPasswordVo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/")
public class LiveStreamUserController {

    @Resource
    public LiveStreamUserService liveStreamUserService;
    @Resource
    RedisService redisService;
   @PatchMapping ("/updatePass/{token}")
    public Result<Boolean> updatePasswordById( @Valid @RequestBody ResetPasswordVo resetPasswordVo, @PathVariable("token") String token){
        LiveStreamUser liveStreamUser = redisService.get(LoginKey.token,token,LiveStreamUser.class);
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        String password = resetPasswordVo.getPassword();
       log.info("id: {}, password: {},token: {}",userId, password,token);
       Boolean res = liveStreamUserService.updatePassword(token,userId,password);
       return Result.success(res);
    }

    @PatchMapping("/updatePassByForgot/{id}")
    public Result<Boolean> updatePassByForgotType(@PathVariable("id")String id, @Valid @RequestBody ResetPasswordVo resetPasswordVo){
        String password = resetPasswordVo.getPassword();
        log.info("update pass by forgot type:id: {}",id);
        Boolean res = liveStreamUserService.updatePasswordByForgotType(Long.parseLong(id),password);
        return Result.success(res);
    }

    @PostMapping("/create")
    public Result<Boolean> createUser(@RequestBody @Valid LiveStreamUserForm liveStreamUserForm){
       liveStreamUserService.save(liveStreamUserForm);
       return Result.success(true);
    }

    @GetMapping("/{id}")
    public Result<LiveStreamUserVo> findUserById(@PathVariable String id) {
       LiveStreamUserVo liveStreamUserVo = liveStreamUserService.findById(Long.parseLong(id));
       return Result.success(liveStreamUserVo);
    }
    @PatchMapping("/updateNickName/{token}/{nickName}")
    public Result<Boolean> updateNickName(@PathVariable String token,@PathVariable String nickName){
        LiveStreamUser liveStreamUser = redisService.get(LoginKey.token,token,LiveStreamUser.class);
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long id = liveStreamUser.getId();
        liveStreamUserService.updateNickName(token,id,nickName);
        return Result.success(true);
    }

    @PatchMapping("/updateHead/{token}")
    public Result<Boolean> updateHead(@PathVariable String token,@RequestBody @Valid ResetHeadForm resetHeadForm){
        LiveStreamUser liveStreamUser = redisService.get(LoginKey.token,token,LiveStreamUser.class);
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long id = liveStreamUser.getId();
        String head = resetHeadForm.getHead();
        liveStreamUserService.updateHead(token,id,head);
        return Result.success(true);
    }


}
