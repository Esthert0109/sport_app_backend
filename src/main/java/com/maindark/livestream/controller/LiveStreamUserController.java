package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.form.LiveStreamUserForm;
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
   @PatchMapping ("/{token}")
    public Result<Boolean> updatePasswordById(LiveStreamUser liveStreamUser, @Valid @RequestBody ResetPasswordVo resetPasswordVo, @PathVariable("token") String token){
        Long userId = liveStreamUser.getId();
        String password = resetPasswordVo.getPassword();
       log.info("id:"+userId +" password:" + password +" token:" + token);
       Boolean res = liveStreamUserService.updatePassword(token,userId,password);
       return Result.success(res);
    }

    @PostMapping("/create")
    public Result<Boolean> createUser(@RequestBody @Valid LiveStreamUserForm liveStreamUserForm){
       liveStreamUserService.save(liveStreamUserForm);
       return Result.success(true);
    }

    @GetMapping("/{id}")
    public Result<LiveStreamUserVo> findUserById(@PathVariable Long id) {
       LiveStreamUserVo liveStreamUserVo = liveStreamUserService.findById(id);
       return Result.success(liveStreamUserVo);
    }
}
