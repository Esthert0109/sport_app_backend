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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/collections")
@CrossOrigin
public class LiveStreamCollectionController {

    @Resource
    LiveStreamCollectionService liveStreamCollectionService;

    @GetMapping("/football/list")
    public Result<List<Map<String,Object>>> getAllFootballCollection(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        List<Map<String,Object>> liveStreamCollectionVos = liveStreamCollectionService.getAllFootballCollectionByUserId(userId,request);
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
    public Result<List<Map<String,Object>>> getAllBasketballCollection(LiveStreamUser liveStreamUser,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if(liveStreamUser == null) {
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Long userId = liveStreamUser.getId();
        List<Map<String,Object>> liveStreamCollectionVos = liveStreamCollectionService.getAllBasketballCollectionByUserId(userId,request);
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
