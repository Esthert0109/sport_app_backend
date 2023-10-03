package com.maindark.livestream.controller;

import com.maindark.livestream.domain.FootballMatch;
import com.maindark.livestream.domain.LiveStreamCollection;
import com.maindark.livestream.domain.LiveStreamUser;
import com.maindark.livestream.form.CollectionForm;
import com.maindark.livestream.result.Result;
import com.maindark.livestream.service.LiveStreamCollectionService;
import com.maindark.livestream.vo.LiveStreamCollectionVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collections")
public class LiveStreamCollectionController {

    @Resource
    LiveStreamCollectionService liveStreamCollectionService;

    @GetMapping("/list")
    public Result<List<LiveStreamCollectionVo>> getAllCollection(LiveStreamUser liveStreamUser) {
        Long userId = liveStreamUser.getId();
        List<LiveStreamCollectionVo> liveStreamCollectionVos = liveStreamCollectionService.getAllCollectionByUserId(userId);
        return Result.success(liveStreamCollectionVos);
    }

    @GetMapping("/football/{matchId}")
    public Result<FootballMatch> getFootballMatch(LiveStreamUser liveStreamUser, @PathVariable Integer matchId){
        FootballMatch footballMatch = liveStreamCollectionService.getFootballMatchByMatchId(matchId);
        return Result.success(footballMatch);
    }

    @GetMapping("/basketball/{matchId}")
    public Result<FootballMatch> getBasketballMatch(LiveStreamUser liveStreamUser, @PathVariable Integer matchId){
        FootballMatch footballMatch = liveStreamCollectionService.getFootballMatchByMatchId(matchId);
        return Result.success(footballMatch);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCollectionById(LiveStreamUser liveStreamUser, @PathVariable Integer id){
        liveStreamCollectionService.deleteCollectionById(id);
        return Result.success(true);
    }

    @PostMapping("/")
    public Result<LiveStreamCollection> createCollection(LiveStreamUser liveStreamUser, @RequestBody CollectionForm collectionForm){
        Long userId = liveStreamUser.getId();
        LiveStreamCollection collection = liveStreamCollectionService.createCollection(userId,collectionForm);
        return Result.success(collection);
    }


}
