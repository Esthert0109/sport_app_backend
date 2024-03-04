package com.maindark.livestream.controller;

import com.maindark.livestream.domain.AllSportsCollection;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.exception.GlobalException;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.result.CodeMsg;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.AllSportsCollectionService;
import com.maindark.livestream.vo.BasketballMatchVo;
import com.maindark.livestream.vo.FootballMatchVo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/en/collections")
public class AllSportsCollectionController {

    @Resource
    AllSportsCollectionService allSportsCollectionService;

    @GetMapping("/football/list")
    public Result<Map<String,List<FootballMatchVo>>> getAllFootballCollection(LiveStreamUser liveStreamUser, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                              @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Long userId = liveStreamUser.getId();
        Map<String,List<FootballMatchVo>> liveStreamCollectionVos = allSportsCollectionService.getAllFootballCollectionByUserId(userId,request);
        return Result.success(liveStreamCollectionVos);
    }
    @GetMapping("/football/list/3")
    public Result<List<FootballMatchVo>> getThreeFootballCollections(LiveStreamUser liveStreamUser) {
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        List<FootballMatchVo> liveStreamCollectionVos = allSportsCollectionService.getThreeFootballCollectionsByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }

    @GetMapping("/basketball/list")
    public Result<Map<String,List<BasketballMatchVo>>> getAllCollection(LiveStreamUser liveStreamUser,@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        PageRequest request = PageRequest.of(page - 1, size, Sort.Direction.DESC,"match_date");
        Long userId = liveStreamUser.getId();
        Map<String,List<BasketballMatchVo>> liveStreamCollectionVos = allSportsCollectionService.getAllBasketballCollectionByUserId(userId,request);
        return Result.success(liveStreamCollectionVos);
    }
    @GetMapping("/basketball/list/3")
    public Result<List<BasketballMatchVo>> getThreeCollections(LiveStreamUser liveStreamUser) {
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        Long userId = liveStreamUser.getId();
        List<BasketballMatchVo> liveStreamCollectionVos = allSportsCollectionService.getThreeBasketballCollectionsByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }

    @PostMapping("/")
    public Result<AllSportsCollection> createCollection(LiveStreamUser liveStreamUser, @RequestBody CollectionForm collectionForm){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
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
    public Result<BasketballMatchVo> getBasketballMatch(LiveStreamUser liveStreamUser, @PathVariable String matchId){
        if(liveStreamUser == null){
            throw new GlobalException(CodeMsg.LOGIN_IN);
        }
        BasketballMatchVo basketballMatchVo = allSportsCollectionService.getBasketballMatchByMatchId(Long.parseLong(matchId));
        return Result.success(basketballMatchVo);
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
