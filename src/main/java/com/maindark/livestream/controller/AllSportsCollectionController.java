package com.maindark.livestream.controller;

import com.maindark.livestream.domain.AllSportsCollection;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsCollectionService;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/en/collections")
public class AllSportsCollectionController {

    @Resource
    AllSportsCollectionService allSportsCollectionService;

    @GetMapping("/list")
    public Result<List<FootballMatchVo>> getAllCollection(LiveStreamUser liveStreamUser) {
        Long userId = liveStreamUser.getId();
        List<FootballMatchVo> liveStreamCollectionVos = allSportsCollectionService.getAllCollectionByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }
    @GetMapping("/list/3")
    public Result<List<FootballMatchVo>> getThreeCollections(LiveStreamUser liveStreamUser) {
        Long userId = liveStreamUser.getId();
        List<FootballMatchVo> liveStreamCollectionVos = allSportsCollectionService.getThreeCollectionsByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }

    @PostMapping("/")
    public Result<AllSportsCollection> createCollection(LiveStreamUser liveStreamUser, @RequestBody CollectionForm collectionForm){
        Long userId = liveStreamUser.getId();
        AllSportsCollection collection = allSportsCollectionService.createCollection(userId,collectionForm);
        return Result.success(collection);
    }


    @GetMapping("/football/{matchId}")
    public Result<FootballMatchVo> getFootballMatch(LiveStreamUser liveStreamUser, @PathVariable String matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        FootballMatchVo footballMatch = allSportsCollectionService.getFootballMatchByMatchId(Long.parseLong(matchId));
        return Result.success(footballMatch);
    }

    @GetMapping("/basketball/{matchId}")
    public Result<FootballMatchVo> getBasketballMatch(LiveStreamUser liveStreamUser, @PathVariable String matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        FootballMatchVo footballMatch = allSportsCollectionService.getFootballMatchByMatchId(Long.parseLong(matchId));
        return Result.success(footballMatch);
    }

    @DeleteMapping("/{matchId}")
    public Result<Boolean> deleteCollectionById(LiveStreamUser liveStreamUser, @PathVariable String matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        allSportsCollectionService.deleteCollectionById(userId,Long.parseLong(matchId));
        return Result.success(true);
    }
}
