package com.maindark.livestream.controller;

import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LiveStreamCollectionService;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collections")
public class LiveStreamCollectionController {

    @Resource
    LiveStreamCollectionService liveStreamCollectionService;

    @GetMapping("/football/list")
    public Result<List<FootballMatchVo>> getAllFootballCollection(LiveStreamUser liveStreamUser) {
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        List<FootballMatchVo> liveStreamCollectionVos = liveStreamCollectionService.getAllFootballCollectionByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }


    @GetMapping("/football/list/3")
    public Result<List<FootballMatchVo>> getThreeFootballCollections(LiveStreamUser liveStreamUser) {
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        List<FootballMatchVo> liveStreamCollectionVos = liveStreamCollectionService.getThreeFootballCollectionsByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }

    @GetMapping("/football/{matchId}")
    public Result<FootballMatchVo> getFootballMatch(LiveStreamUser liveStreamUser, @PathVariable Integer matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        FootballMatchVo footballMatch = liveStreamCollectionService.getFootballMatchByMatchId(matchId);
        return Result.success(footballMatch);
    }


    @GetMapping("/basketball/list")
    public Result<List<BasketballMatchVo>> getAllBasketballCollection(LiveStreamUser liveStreamUser) {
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        List<BasketballMatchVo> liveStreamCollectionVos = liveStreamCollectionService.getAllBasketballCollectionByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }

    @GetMapping("/basketball/list/3")
    public Result<List<BasketballMatchVo>> getThreeBasketFootballCollections(LiveStreamUser liveStreamUser) {
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        List<BasketballMatchVo> liveStreamCollectionVos = liveStreamCollectionService.getThreeBasketballCollectionsByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }


    @GetMapping("/basketball/{matchId}")
    public Result<BasketballMatchVo> getBasketballMatch(LiveStreamUser liveStreamUser, @PathVariable Integer matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        BasketballMatchVo basketballMatchVo = liveStreamCollectionService.getBasketballMatchByMatchId(matchId);
        return Result.success(basketballMatchVo);
    }


    @DeleteMapping("/{matchId}")
    public Result<Boolean> deleteCollectionById(LiveStreamUser liveStreamUser, @PathVariable Integer matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        liveStreamCollectionService.deleteCollectionById(userId,matchId);
        return Result.success(true);
    }


    @PostMapping("/")
    public Result<LiveStreamCollection> createCollection(LiveStreamUser liveStreamUser, @RequestBody CollectionForm collectionForm){
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        LiveStreamCollection collection = liveStreamCollectionService.createCollection(userId,collectionForm);
        return Result.success(collection);
    }
    
}
